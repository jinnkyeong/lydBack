package com.loveyourdog.brokingservice.model.dto.requestDto;
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
public class CommisionRequestDto {


    private Long customerId;
    @ApiModelProperty(example="wlsrud0303@naver.com")
    private String email; // 필요
    private String pwd;

    @ApiModelProperty(example="김은정")
    private String name;
    @ApiModelProperty(example="1998")
    private int birthYear;
    private int age;
    @ApiModelProperty(example="f")
    private String gen; // m or f
    @ApiModelProperty(example="01010333333")
    private String phone;
    @ApiModelProperty(example="tfue")
    private String nick;
    @ApiModelProperty(example="경기도")
    private String addrState;
    @ApiModelProperty(example="수원시")
    private String addrTown;
    @ApiModelProperty(example="223동 333호")
    private String addrDetail;
    














    @ApiModelProperty(example="3334")
    private int price; // 희망이용금액
    @ApiModelProperty(example="2")
    private int month; // 산책일자 월
    @ApiModelProperty(example="2")
    private int day; // 산책일자 일
    @ApiModelProperty(example="11")
    private int startHour; // 산책시작 시
    @ApiModelProperty(example="30")
    private int startMin; // 산책시작 분
    @ApiModelProperty(example="16")
    private int endHour; // 산책종료 시
    @ApiModelProperty(example="00")
    private int endMin; // 산책종료 분

    @ApiModelProperty(example="미미")
    private String dogName;
    @ApiModelProperty(example="30")
    private int dogAge;
    @ApiModelProperty(example="15.3")
    private int dogWeight;
    @ApiModelProperty(example="말티즈")
    private String breed;
    @ApiModelProperty(example="1")
    private int dogType; // 1소형견 2중형견 3대형견 4초대형견
    @ApiModelProperty(example="3")
    private int dogAggr; // 공격성 1~5
    @ApiModelProperty(example="4")
    private int dogHealth; // 건강상태 1~5
    @ApiModelProperty(example="금암공원 산책로")
    private String road; // 지정산책로
    
    private List<String> cusRequires; // 요청사항
    private List<String> certificateKeywords; // 자격증 명 목록

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
