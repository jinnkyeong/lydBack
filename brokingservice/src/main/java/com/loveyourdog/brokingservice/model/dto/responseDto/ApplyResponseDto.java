package com.loveyourdog.brokingservice.model.dto.responseDto;

import com.loveyourdog.brokingservice.model.dto.querydsl.CertificateDto;
import com.loveyourdog.brokingservice.model.dto.querydsl.LocationDto;
//import com.loveyourdog.brokingservice.model.dto.querydsl.WeekdayDto;
import com.loveyourdog.brokingservice.model.dto.querydsl.WeekdayDto;
import com.loveyourdog.brokingservice.model.entity.Application;
import com.loveyourdog.brokingservice.model.entity.Certificate;
import com.loveyourdog.brokingservice.model.entity.Location;
import com.loveyourdog.brokingservice.model.entity.Weekday;
import com.loveyourdog.brokingservice.model.enums.AccountType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ApplyResponseDto {


    // dogwalker
    private Long dogwalkerId; //

    private AccountType accountType; // 계정 유형

    private String email;

    private String name;

    private int birthYear;

    private String gen; // m or f

    private String phone;

    private String nick;

    private String addrState;
    private String addrTown;
    private String addrDetail;

    private boolean interviewPassed; // 인터뷰 통과 여부

    private boolean appicationPassed; // 지원서 통과 여부

    private boolean passed; // 최종합격여부

    private String depositBank; // 입금 은행

    private String depositAccount; // 입금 계좌번호(번호만)

    private String dirName; // S3 객체 이름

    private String fileName; // 이미지 파일 이름

    private String extension; // 파일 확장자
    private int view;








    // application
    private Long applicationId;

    private List<LocationDto> locations; // 거주지 외 할동가능지역

    private List<WeekdayDto> weekdays;// 근무 가능 요일 (sun,mon,tue,wed,thu,fri,sat 중 하나)

    private List<CertificateDto> certificates;

    private boolean adopted; // 반려경험 유무

    private int adoptedWhich; // 키웠던 동물
    // 1 고양이 2 강아지 3 그외

    private int adoptedMonth; // 반려기간(개월)

    private int petType; // 희망하는 강아지 유형

    private int price; // 희망하는 이용금액

    private String sentence; // 각오의 한마디


    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
