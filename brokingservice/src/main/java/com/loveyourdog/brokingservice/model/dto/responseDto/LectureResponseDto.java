package com.loveyourdog.brokingservice.model.dto.responseDto;

import com.loveyourdog.brokingservice.model.entity.Dogwalker;
import lombok.*;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LectureResponseDto {

    private Long id;
    // 1 : 도그워킹 기술
    // 2: 반려동물 행동 심리
    // 3: 기초 훈련방법
    // 4: 반려견 산책 유의사항
    private String title;


    private Long lectureId;
    private LocalDateTime endAt; // 해당 강의를 완강한 일시
    private Long dogwalkerId;



}
