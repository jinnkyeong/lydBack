package com.loveyourdog.brokingservice.model.dto.requestDto;

import com.loveyourdog.brokingservice.model.dto.querydsl.LocationDto;
import com.loveyourdog.brokingservice.model.entity.Location;
import com.loveyourdog.brokingservice.model.enums.AccountType;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplyModRequestDto {



    // dogwalker



    private String name;
    private int age; // 생년월일로 구하기
    private String gen; // m or f
    private String addrState;
    private String addrTown;


    // application
    private List<LocationDto> locations; // 거주지 외 할동가능지역
    private List<String> deleteLocations; // 거주지 외 할동가능지역
    private List<String> weekdayKeywords;// 근무 가능 요일 (sun,mon,tue,wed,thu,fri,sat 중 하나)
    private List<String> deleteWeekdays;//
    private List<String> certificateKeywords;
    private List<String> deletedCertificates; // 삭제될 자격증 id
    private boolean adopted; // 반려경험 유무
    private String adoptedWhich; // 키웠던 동물(String으로 변환해서 보냄) // 1 고양이 2 강아지 3 기타
    private int adoptedMonth; // 반려기간(개월)
    private String petType; // 희망하는 강아지 유형(String으로 변환해서 보냄)
    private int price; // 희망하는 이용금액
    private String sentence; // 각오의 한마디



    //    모든 변수가 NULL 이면 true
    //    하나라도 NULL이 아닌 변수가 있으면 false
    public boolean isDtoEntireVariableNull() {
        try {
            for (Field f : getClass().getDeclaredFields()) {
                if (f.get(this) != null) {
                    return false;
                }
            }
            return true;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
