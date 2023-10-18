package com.loveyourdog.brokingservice.repository.commision;//package com.loveyourdog.brokingservice.repository.customImpl;

import com.loveyourdog.brokingservice.model.entity.*;
import com.loveyourdog.brokingservice.repository.application.ApplicationRepositoryCustom;
import com.loveyourdog.brokingservice.repository.application.ApplicationSearchCondition;
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

@Repository
public class CommisionRepositoryImpl implements CommisionRepositoryCustom {

    private JPAQueryFactory queryFactory;
    private QCommision commision;

    public CommisionRepositoryImpl(EntityManager em) {
        this.queryFactory =  new JPAQueryFactory(em);
        this.commision = QCommision.commision;
    }


    public Page<Commision> search(CommisionSearchCondition condition, PageRequest page) {
        List<Commision> content = queryFactory
                .select(commision)
                .from(commision)
                .where(
                        certificateEq(condition.getCertificateKeywords()), // 자격증이 하나라도 겹치면
                        weekdayEq(condition.getWeekdayNames()),
                        priceBetween(condition.getMinimalPrice(), condition.getMaximalPrice(), condition.isSelectAverage()), // 최소-최대 범위에 들면
                        dogAggrEq(condition.getDogAggrs()), // 공격성이 하나라도 있으면
                        dogTypeEq(condition.getDogTypes()), // 소중대 타입이 하나라도 있으면
                        timeEq(condition.getStartHour(),condition.getStartMin(),
                                condition.getEndHour(),condition.getEndMin())
                )
                .offset(page.getOffset())
                .limit(page.getPageSize())
                .orderBy(sort(page)) // 정렬
                .fetch();

        JPAQuery<Commision> countQuery = queryFactory
                .selectFrom(commision)
                .where(
                        certificateEq(condition.getCertificateKeywords()), // 자격증이 하나라도 겹치면
                        weekdayEq(condition.getWeekdayNames()),
                        priceBetween(condition.getMinimalPrice(), condition.getMaximalPrice(), condition.isSelectAverage()), // 최소-최대 범위에 들면
                        dogAggrEq(condition.getDogAggrs()), // 공격성이 하나라도 있으면
                        dogTypeEq(condition.getDogTypes()), // 소중대 타입이 하나라도 있으면
                        timeEq(condition.getStartHour(),condition.getStartMin(),
                                condition.getEndHour(),condition.getEndMin())
                );

        return PageableExecutionUtils.getPage(content, page, () -> countQuery.fetchCount());

    }



    private OrderSpecifier[] sort(PageRequest page) {
//    private OrderSpecifier<?> sort(PageRequest page) {
        //서비스에서 보내준 Pageable 객체에 정렬조건 null 값 체크

        List<OrderSpecifier> orderSpecifiers = new ArrayList<>();

        if (!page.getSort().isEmpty()) {
            //정렬값이 들어 있으면 for 사용하여 값을 가져온다
            for (Sort.Order order : page.getSort()) {
                // 서비스에서 넣어준 DESC or ASC 를 가져온다.
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                // 서비스에서 넣어준 정렬 조건을 스위치 케이스 문을 활용하여 셋팅하여 준다.
                switch (order.getProperty()){
                    case "price":
                        orderSpecifiers.add(new OrderSpecifier(direction, commision.price));
                    case "star":
                        orderSpecifiers.add(new OrderSpecifier(direction, commision.customer.temperture));
                    case "view":
                        orderSpecifiers.add(new OrderSpecifier(direction, commision.view));
                }

            }
        }
        return orderSpecifiers.toArray(OrderSpecifier[]::new);


    }





    // Expressions
    // 자격증 중 하나라도 겹치는게 있는 사람
    private BooleanExpression certificateEq(List<String> certificates){
        // 안 넘어온 경우 null
        if(certificates==null  || certificates.size() <= 0){
            System.out.println("cererr null!!");
            return null;
        } else {
            for (String w: certificates) {
                if(!StringUtils.hasText(w)){ // "" 로 넘어온 것도 처리
                    System.out.println("cer null!!");
                    return null;
                }
            }
            return commision.customer.wishCertificates.any().in(certificates);
        }
    }
    // 요일 중 하나라도 겹치는게 있는 사람

    private BooleanExpression weekdayEq(List<String> weekdays){
        if(weekdays==null  || weekdays.size() <= 0 ){
            System.out.println("wekekeek null!!");
            return null;

        } else {
            for (String w: weekdays) {
                if(!StringUtils.hasText(w)){
                    System.out.println("week null!!");
                    return null;
                }
            }
            return commision.weekday.in(weekdays);
        }
    }
     //가격대가 맞는 사람
    private BooleanExpression priceMinimal(int min){
            return commision.price.goe(min);
    }
    private BooleanExpression priceMaximal(int max){
            return commision.price.loe(max);

    }
    private BooleanExpression priceBetween(int min, int max, boolean op){
        if(!StringUtils.hasText(String.valueOf(min))
        || !StringUtils.hasText(String.valueOf(min))
        || min==0 || max==0){
            return null;
        }
        if(op){
            return commision.price.eq(15000); // 임의로 정해봄
        } else {
            return priceMinimal(min).and(priceMaximal(max));
        }
    }



    // 공격성
    private BooleanExpression dogAggrEq(List<Integer> aggrs){
        if(aggrs==null  || aggrs.size() <= 0){
            return null;
        } else {
            for (int w: aggrs) {
                if(!StringUtils.hasText(String.valueOf(w)) || w==0){
                    System.out.println("aggr null!!");
                    return null;
                }
            }
            return commision.dogAggr.in(aggrs);
        }
    }

    private BooleanExpression dogTypeEq(List<Integer> types){
        if( types==null  || types.size() <= 0 ){
            System.out.println("dogtype nu;;;");
            return null;
        } else {
            for (int type: types) {
                if(!StringUtils.hasText(String.valueOf(type)) || type == 0){
                    System.out.println("dogtype null");
                    return null;
                }
            }
            return commision.dogType.in(types);
        }
    }

    private BooleanExpression timeEq(int startH, int startM, int endH, int endM){
        if(!StringUtils.hasText(String.valueOf(startH))
        || !StringUtils.hasText(String.valueOf(startM))
        || !StringUtils.hasText(String.valueOf(endH))
        || !StringUtils.hasText(String.valueOf(endM))
        || startH==0 || startM==0 || endH==0 || endM==0)
        {
            System.out.println("time null/////");
            return null;
        }
        // 0으로 들어오는 것도 null 해야 할까?
        return commision.startHour.goe(startH).and(commision.endHour.loe(endH));
    }

}
