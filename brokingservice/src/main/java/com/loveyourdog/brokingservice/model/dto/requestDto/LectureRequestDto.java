package com.loveyourdog.brokingservice.model.dto.requestDto;

import com.loveyourdog.brokingservice.model.entity.Dogwalker;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LectureRequestDto {

    private Long dogwalkerId;
    private Long lectureTypeId;
    // 1 : 도그워킹 기술
    // 2: 반려동물 행동 심리
    // 3: 기초 훈련방법
    // 4: 반려견 산책 유의사항
    private LocalDateTime endAt; // 해당 강의를 완강한 일시



}
