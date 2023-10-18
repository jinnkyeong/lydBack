package com.loveyourdog.brokingservice.repository.commision;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommisionSearchCondition {
    private int page;
    private int size;
    private List<String> orderStr;

    // 자격증 키워드 리스트
    private List<String> certificateKeywords;
    // 요일 리스트
    private List<String> weekdayNames;
    // 최소가격
    private int minimalPrice;
    // 최대가격
    private int maximalPrice;
    // 평균가격 선택 여부
    private boolean selectAverage;

    private int startHour;
    private int startMin;
    private int endHour;
    private int endMin;
    private List<Integer>  dogTypes;
    private List<Integer>  dogAggrs;
}
