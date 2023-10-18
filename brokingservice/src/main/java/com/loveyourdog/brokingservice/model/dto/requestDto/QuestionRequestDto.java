package com.loveyourdog.brokingservice.model.dto.requestDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Set;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionRequestDto {

//    private Long dogwalkerId;
////    private int testScore; // 점수
//    private LocalDateTime testStartAt; // 시작일시
//    private LocalDateTime testEndAt; // 종료일시

    private Long questionTypeId;
    private String input;


}
