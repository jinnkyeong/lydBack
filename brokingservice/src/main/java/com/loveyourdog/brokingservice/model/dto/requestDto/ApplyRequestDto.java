package com.loveyourdog.brokingservice.model.dto.requestDto;

import com.loveyourdog.brokingservice.model.dto.querydsl.LocationDto;
import com.loveyourdog.brokingservice.model.enums.AccountType;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplyRequestDto {


    // dogwalker
    @ApiModelProperty(example="5")
    private Long dogwalkerId;


    @ApiModelProperty(example="wlsrud0303@naver.com")
    private String email;
    @ApiModelProperty(example="wlsrud0303")
    private String pwd;
    @ApiModelProperty(example="진경")
    private String name;
    @ApiModelProperty(example="1998")
    private int birthYear;
    @ApiModelProperty(example="f")
    private String gen; // m or f
    @ApiModelProperty(example="01067777777")
    private String phone;
    @ApiModelProperty(example="닉네임")
    private String nick; /// 지울까
    @ApiModelProperty(example="경기도")
    private String addrState;
    @ApiModelProperty(example="오산시 여계산로 21")
    private String addrTown;
    @ApiModelProperty(example="603동 123호")
    private String addrDetail;

    private boolean interviewPassed; // 인터뷰 통과 여부
    private boolean appicationPassed; // 지원서 통과 여부
    private boolean passed; // 최종합격여부

    @ApiModelProperty(example="신한")
    private String depositBank; // 입금 은행
    @ApiModelProperty(example="48754389989475")
    private String depositAccount; // 입금 계좌번호(번호만)

    @ApiModelProperty(example="[훈련사]")
    //@JsonIgnore 이거하면 요청 바디에 아예 안들어감
    private List<String> certificateKeywords; // 자격증 이름

    private String dirName; // S3 객체 이름

    private String fileName; // 이미지 파일 이름

    private String extension; // 파일 확장자








    // application

    @ApiModelProperty(example="true")
    private boolean adopted; // 반려경험 유무
    @ApiModelProperty(example="2")
    private int adoptedWhich; // 키웠던 동물
    // 1 고양이 2 강아지 3 그외
    @ApiModelProperty(example="34")
    private int adoptedMonth; // 반려기간(개월)
    @ApiModelProperty(example="2")
    private int petType; // 희망하는 강아지 유형
    @ApiModelProperty(example="15000")
    private int price; // 희망하는 이용금액
    @ApiModelProperty(example="화이팅!")
    private String sentence; // 각오의 한마디




    // location
    private List<LocationDto> locations;
//    private String state; // 시도
//    private String town; // 시군구

    // weekday=
    private List<String> weekdayNames; // 근무 가능 요일 (sun,mon,tue,wed,thu,fri,sat 중 하나)


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
