package com.loveyourdog.brokingservice.repository.application;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationSearchCondition {

    private int page;
    private int size;
    private List<String> orderStr;

    // 자격증 이름 리스트
    private List<String> certificateKeywords;
    // 날짜
    private int month;
    private int day;

    // 최소가격
    private int minimalPrice;
    // 최대가격
    private int maximalPrice;
    // 평균가격 선택 여부
    private boolean selectAverage;
    // 연령대 리스트
    private List<Integer> ages;
    // 성별
    private String gen;
}
