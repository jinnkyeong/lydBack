package com.loveyourdog.brokingservice.repository.application;//package com.loveyourdog.brokingservice.repository.customImpl;

import com.amazonaws.services.s3.model.lifecycle.LifecycleObjectSizeGreaterThanPredicate;
import com.loveyourdog.brokingservice.model.dto.querydsl.ApplicationDto;
import com.loveyourdog.brokingservice.model.dto.querydsl.DogwalkerDto;

import com.loveyourdog.brokingservice.model.entity.*;
import com.querydsl.core.QueryResults;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.*;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

@Repository
public class ApplicationRepositoryImpl implements ApplicationRepositoryCustom {

    private JPAQueryFactory queryFactory;
    private QDogwalker dogwalker;
    private QApplication application;
    private QCertificate certificate;
//    private QApplicationDto qApplicationDto;

//    private QApplicationDto qApplicationDto;
    private QWeekday weekday;



    public ApplicationRepositoryImpl(EntityManager em) {
        this.queryFactory =  new JPAQueryFactory(em);
        this.dogwalker = QDogwalker.dogwalker;
        this.application = QApplication.application;
        this.certificate = QCertificate.certificate;
        this.weekday = QWeekday.weekday;
    }


    public Page<Application> search(ApplicationSearchCondition condition, PageRequest page) {

        System.out.println("[ impl ]");
        List<Application> content = queryFactory
                .select(application)
                .from(application)
                .leftJoin(certificate).on(application.dogwalker.eq(certificate.dogwalker))
                .fetchJoin()
                .distinct()
                .where(
                        certificateEq(condition.getCertificateKeywords()), // 자격증이 하나라도 겹치면
                        weekdayEq(condition.getMonth(), condition.getDay()),
                        priceBetween(condition.getMinimalPrice(), condition.getMaximalPrice(), condition.isSelectAverage()), // 최소-최대 범위에 들면
                        ageEq(condition.getAges()), // 나이대가 맞으면
                        genEq(condition.getGen()), // 성별이 동일하면
                        application.dogwalker.passed.eq(true)
                )
                .offset(page.getOffset())
                .limit(page.getPageSize())
                .orderBy(sort(page)) // 정렬
                .fetch();

        JPAQuery<Application> countQuery = queryFactory
                .selectFrom(application)
                .where(
                        certificateEq(condition.getCertificateKeywords()), // 자격증이 하나라도 겹치면
                        weekdayEq(condition.getMonth(),condition.getDay()),
                        priceBetween(condition.getMinimalPrice(), condition.getMaximalPrice(), condition.isSelectAverage()), // 최소-최대 범위에 들면
                        ageEq(condition.getAges()), // 나이대가 맞으면
                        genEq(condition.getGen()) // 성별이 동일하면
                );
        return PageableExecutionUtils.getPage(content, page, () -> countQuery.fetchCount());

//        return new PageImpl<>(content,pageable,content.size());
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
                        orderSpecifiers.add(new OrderSpecifier(direction, application.price));
                    case "star":
                        orderSpecifiers.add(new OrderSpecifier(direction, application.dogwalker.star));
                    case "view":
                        orderSpecifiers.add(new OrderSpecifier(direction, application.view));
                }

            }
        }
        return orderSpecifiers.toArray(OrderSpecifier[]::new);
//        return null;
//        PathBuilder<Application> entityPath = new PathBuilder<>(Application.class, "application");
//        return page.getSort() // (2) pageale.getSort()는 절대 null을 반환하지 않습니다. 아무 것도 입력하지 않은 경우 Sort.unsorted()를 호출해 상수 UNSORTED를 설정해주기 때문입니다. 따라서 별도로 null 체크를 할 필요가 없습니다.
//                .stream() // (3) Sort는 Streamable을 구현하고 있기 때문에 바로 stream()을 호출할 수 있습니다.
//                .map(order -> new OrderSpecifier(Order.valueOf(order.getDirection().name()), entityPath.get(order.getProperty()))) // (4) orderBy에 전달해야 할 타입이 OrderSpecifier이기 때문에 해당 타입으로 매핑해줍니다.
//                .toArray(OrderSpecifier[]::new); // (5) orderBy에는 0..N 개의 OrderSpecifier를 전달할 수 있습니다. Sort에 Order가 존재할 가능성(N개가 될 가능성)이 있기 때문에 배열 타입으로 변환해줍니다.

    }










    // Expressions
    // 자격증 중 하나라도 겹치는게 있는 사람
    private BooleanExpression certificateEq(List<String> certificates){
        // 안 넘어온 경우 null
        if(certificates==null || certificates.size() <= 0 ||  !StringUtils.hasText("certificates")){
            return null;
        } else {
            for (String w: certificates) {
                if(!StringUtils.hasText(w)){ // "" 로 넘어온 것도 처리
                    return null;
                }
            }
            return application.dogwalker.certificates.any().keyword.in(certificates);
        }
    }
    // 요일 중 하나라도 겹치는게 있는 사람
    private BooleanExpression weekdayEq(int month, int day){
        if(!StringUtils.hasText(String.valueOf(month)) // month, day 모두 입력하게 해야 함
                || !StringUtils.hasText(String.valueOf(day))
                || month==0 || day==0){
            return null;
        }

        int thisYear = LocalDate.now().getYear(); // 올해
        LocalDate date = LocalDate.of(thisYear, month, day); // 해당일자
        String dayOfWeek = date.getDayOfWeek()
                .getDisplayName(TextStyle.SHORT, Locale.US)
                .toLowerCase(); // 요일(mon,tue...)

        return application.weekdays.any().day.eq(dayOfWeek);

    }
    // 가격대가 맞는 사람
    private BooleanExpression priceMinimal(int min){

            return application.price.goe(min);

    }
    private BooleanExpression priceMaximal(int max){

            return application.price.loe(max);

    }
    private BooleanExpression priceBetween(int min, int max, boolean op){
        if(!StringUtils.hasText(String.valueOf(min))
                || !StringUtils.hasText(String.valueOf(min))
                || min==0 || max==0){
            return null;
        }
        if(op){
            return application.price.eq(15000); // 임의로 정해봄
        } else {
            return priceMinimal(min).and(priceMaximal(max));
        }
    }
    // 나이대가 하나라도 겹치는 사람
    private BooleanExpression ageEq(List<Integer> ages){
        if(ages==null || ages.size()<=0){
            return null;
        } else {
            for (Integer w: ages) {
                if(!StringUtils.hasText(String.valueOf(w))){
                    return null;
                }
                if(w==0){
                    return null;
                }
            }
            return application.dogwalker.birthYear
                    .subtract(2023).multiply(-1).divide(10).floor().multiply(10) // 예) 11세, 19세 -> 10대
                    .in(ages);
        }
    }
    // 성별이 일치하는 사람
    private BooleanExpression genEq(String gen) {
        return StringUtils.hasText(gen) ? application.dogwalker.gen.eq(gen) : null;
    }






}
