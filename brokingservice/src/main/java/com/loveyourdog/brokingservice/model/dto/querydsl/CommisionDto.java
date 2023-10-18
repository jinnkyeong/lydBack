package com.loveyourdog.brokingservice.model.dto.querydsl;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;
import java.util.List;

@Getter
@Setter
@Builder
public class CommisionDto {

    private Long commisionId;
    private String name;
    private String nick;
    private String gen;
    private int birthYear;
    private String addrState;
    private String addrTown;
    private String addrDetail;
    private int temperture;

    private int price;
    private String phone;
    private String email;

    private int month;
    private int day;
    private String weekday;
    private int startHour; // 산책시작 시
    private int startMin; // 산책시작 분
    private int endHour; // 산책종료 시
    private int endMin; // 산책종료 분
    private String dogName;
    private int dogAge;
    private int dogWeight;
    private int dogType; // 1소형견 2중형견 3대형견 4초대형견
    private String breed; // 견종
    private int dogAggr; // 공격성 1~5
    private int dogHealth; // 건강상태 1~5
    private String road; // 지정산책로
    private int view;
    private List<CusRequireDto> cusRequires;
//    private List<InquiryDto> inquiries;
//    private List<OfferDto> offers;


    private String dirName;
    private String fileName;
    private String extension;





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
