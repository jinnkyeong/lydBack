//package com.loveyourdog.brokingservice.repository.alarm;//package com.loveyourdog.brokingservice.repository.customImpl;
//
//import com.loveyourdog.brokingservice.model.entity.*;
//import com.loveyourdog.brokingservice.repository.application.ApplicationRepositoryCustom;
//import com.loveyourdog.brokingservice.repository.application.ApplicationSearchCondition;
//import com.loveyourdog.brokingservice.websocket.AlarmMessage;
//import com.querydsl.core.types.Order;
//import com.querydsl.core.types.OrderSpecifier;
//import com.querydsl.core.types.dsl.BooleanExpression;
//import com.querydsl.jpa.impl.JPAQueryFactory;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.stereotype.Repository;
//import org.springframework.util.StringUtils;
//
//import javax.persistence.EntityManager;
//import java.time.LocalDate;
//import java.time.format.TextStyle;
//import java.util.List;
//import java.util.Locale;
//
//@Repository
//public class AlarmRepositoryImpl implements AlarmRepositoryCustom {
//
//    private JPAQueryFactory queryFactory;
////    private QDogwalker dogwalker;
//
//    private QAlarm alarm;
//
//    public AlarmRepositoryImpl(EntityManager em) {
//        this.queryFactory =  new JPAQueryFactory(em);
////        this.dogwalker = QDogwalker.dogwalker;
//        this.alarm = QAlarm.alarm;
//
//    }
//
//
//    public List<Application> search(ApplicationSearchCondition condition) {
//
//        List<Alarm> alarms = queryFactory
//                .select(alarm)
//                .from(alarm)
//                .where(
//                        StringUtils.hasText(gen) ? alarm..eq(gen) : null,
//                        weekdayEq(condition.getMonth(),condition.getDay()),
//                        priceBetween(condition.getMinimalPrice(), condition.getMaximalPrice(), condition.isSelectAverage()), // 최소-최대 범위에 들면
//                        ageEq(condition.getAges()), // 나이대가 맞으면
//                        genEq(condition.getGen()) // 성별이 동일하면
//                )
////                .orderBy(PostSort(pageable))
////                .offset(pageable.getOffset())   // (2) 페이지 번호
////                .limit(pageable.getPageSize())  // (3) 페이지 사이즈
//                .fetch();
////        return new PageImpl<>(content,pageable,content.size());
//        return content;
//    }
//
//    @Override
//    public List<AlarmMessage> findAlarmsByUserIdAndType(String userId, String userType) {
//        return null;
//    }
//
//
//
//}
