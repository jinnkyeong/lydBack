package com.loveyourdog.brokingservice.model.dto.responseDto;

import com.loveyourdog.brokingservice.model.dto.querydsl.CertificateDto;
import com.loveyourdog.brokingservice.model.dto.querydsl.LocationDto;
//import com.loveyourdog.brokingservice.model.dto.querydsl.WeekdayDto;
import com.loveyourdog.brokingservice.model.entity.Dogwalker;
import com.loveyourdog.brokingservice.model.enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DogwalkerResponseDto {


    // dogwalker
    private Long dogwalkerId;
    private boolean interviewPassed; // 인터뷰 통과 여부
    private String nick;
    private boolean lecturePassed;
    private boolean testPassed;
    private boolean appicationPassed; // 지원서 통과 여부
    private boolean passed; // 최종합격여부
    private int testScore; // 점수
    private LocalDateTime testStartAt; // 시작일시
    private LocalDateTime testEndAt; // 종료일시

    public DogwalkerResponseDto(Dogwalker dogwalker) {
        this.dogwalkerId = dogwalker.getId();
        this.interviewPassed = dogwalker.isInterviewPassed();
        this.nick = dogwalker.getNick();
        if(dogwalker.getLectures().size()==3) { // 임의로 하드코딩(전체 강의가 3개라고 가정
            this.lecturePassed = true;
        } else {
            this.lecturePassed = false;
        }
        if(dogwalker.getTestScore()>=80){
            this.testPassed = true;
        } else {
            this.testPassed = false;
        }
        this.appicationPassed = dogwalker.isAppicationPassed();
        this.passed = dogwalker.isPassed();
    }
}
