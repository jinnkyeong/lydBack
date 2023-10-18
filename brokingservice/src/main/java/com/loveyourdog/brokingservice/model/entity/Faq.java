package com.loveyourdog.brokingservice.model.entity;

import com.loveyourdog.brokingservice.model.dto.responseDto.FaqResponseDto;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Faq {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int category;
    // 1 : 기타
    // 2 : 산책 서비스
    // 3 : 이용요금
    // 4 : 예약 취소 및 변경
    // 5 : 도그워커 지원
    // 6 : 커뮤니티

    private String question;
    private String answer;


    public FaqResponseDto toDto(){
        FaqResponseDto dto = FaqResponseDto.builder()
                .id(getId())
                .category(getCategory())
                .question(getQuestion())
                .answer(getAnswer())
                .build();
        return dto;
    }


}
