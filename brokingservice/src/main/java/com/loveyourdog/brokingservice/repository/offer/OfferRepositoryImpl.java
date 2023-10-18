package com.loveyourdog.brokingservice.repository.offer;//package com.loveyourdog.brokingservice.repository.customImpl;

import com.loveyourdog.brokingservice.model.entity.*;
import com.loveyourdog.brokingservice.repository.inquiry.InquiryCondition;
import com.loveyourdog.brokingservice.repository.inquiry.InquiryRepositoryCustom;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Repository
public class OfferRepositoryImpl implements OfferRepositoryCustom {

    private JPAQueryFactory queryFactory;
    private QDogwalker dogwalker;
    private QApplication application;
    private QInquiry inquiry;
    private QOffer offer;
    private QCertificate certificate;

    private QWeekday weekday;



    public OfferRepositoryImpl(EntityManager em) {
        this.queryFactory =  new JPAQueryFactory(em);
        this.dogwalker = QDogwalker.dogwalker;
        this.application = QApplication.application;
        this.offer = QOffer.offer;
        this.certificate = QCertificate.certificate;
        this.weekday = QWeekday.weekday;
    }


    public Page<Offer> search(InquiryCondition condition, PageRequest page) {

        if(condition.getUserType().equalsIgnoreCase("dogwalker")){
            List<Offer> content = queryFactory
                    .select(offer)
                    .from(offer)
                    .distinct()
                    .where(
                            offer.dogwalker.id.eq(condition.getUserId()),
                            keyEq(condition.getKey())
                    )
                    .offset(page.getOffset())
                    .limit(page.getPageSize())
                    .orderBy(sort(page, condition.getUserType())) // 정렬
                    .fetch();

            JPAQuery<Offer> countQuery = queryFactory
                    .selectFrom(offer)
                    .where(
                            offer.dogwalker.id.eq(condition.getUserId()),
                            keyEq(condition.getKey())
                    );
            return PageableExecutionUtils.getPage(content, page, () -> countQuery.fetchCount());

        } else if(condition.getUserType().equalsIgnoreCase("customer")){
            List<Offer> content = queryFactory
                    .select(offer)
                    .from(offer)
                    .distinct()
                    .where(
                            offer.customer.id.eq(condition.getUserId()),
                            keyEq(condition.getKey())
                    )
                    .offset(page.getOffset())
                    .limit(page.getPageSize())
                    .orderBy(sort(page, condition.getUserType())) // 정렬
                    .fetch();

            JPAQuery<Offer> countQuery = queryFactory
                    .selectFrom(offer)
                    .where(
                            offer.customer.id.eq(condition.getUserId()),
                            keyEq(condition.getKey())
                    );
            return PageableExecutionUtils.getPage(content, page, () -> countQuery.fetchCount());
        } else {
            throw new NoSuchElementException("dw or cus이어야합니다");
        }


    }

    private OrderSpecifier[] sort(PageRequest page, String userType) {
        //서비스에서 보내준 Pageable 객체에 정렬조건 null 값 체크
        List<OrderSpecifier> orderSpecifiers = new ArrayList<>();
        if(userType.equalsIgnoreCase("dogwalker")) {
            if (!page.getSort().isEmpty()) {
                //정렬값이 들어 있으면 for 사용하여 값을 가져온다
                for (Sort.Order order : page.getSort()) {
                    // 서비스에서 넣어준 DESC or ASC 를 가져온다.
                    Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                    // 서비스에서 넣어준 정렬 조건을 스위치 케이스 문을 활용하여 셋팅하여 준다.
                    switch (order.getProperty()) {
                        case "price":
                            orderSpecifiers.add(new OrderSpecifier(direction, offer.price));
                        case "star":
                            orderSpecifiers.add(new OrderSpecifier(direction, offer.commision.customer.temperture));
                        case "view":
                            orderSpecifiers.add(new OrderSpecifier(direction, offer.commision.view));
                    }
                }
            }
        } else if(userType.equalsIgnoreCase("customer")) {
            if (!page.getSort().isEmpty()) {
                //정렬값이 들어 있으면 for 사용하여 값을 가져온다
                for (Sort.Order order : page.getSort()) {
                    // 서비스에서 넣어준 DESC or ASC 를 가져온다.
                    Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                    // 서비스에서 넣어준 정렬 조건을 스위치 케이스 문을 활용하여 셋팅하여 준다.
                    switch (order.getProperty()) {
                        case "price":
                            orderSpecifiers.add(new OrderSpecifier(direction, offer.price));
                        case "star":
                            orderSpecifiers.add(new OrderSpecifier(direction, offer.application.dogwalker.star));
                        case "view":
                            orderSpecifiers.add(new OrderSpecifier(direction, offer.application.view));
                    }
                }
            }
        } else {
            throw new NoSuchElementException("dw or cus이어야합니다");
        }
        return orderSpecifiers.toArray(OrderSpecifier[]::new);
    }



    private BooleanExpression keyEq(int key){
        // 안 넘어온 경우 null
        if(key==0 ||  !StringUtils.hasText(String.valueOf(key))){
            return null;
        } else {
            if(key==1){
                return offer.status.in(1,4);
            } else if(key==2){
                return offer.status.eq(0);
            } else{
                return offer.status.in(3);
            }
        }
    }








}
