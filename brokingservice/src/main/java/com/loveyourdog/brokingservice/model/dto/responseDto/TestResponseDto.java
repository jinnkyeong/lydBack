package com.loveyourdog.brokingservice.model.dto.responseDto;

import com.loveyourdog.brokingservice.model.entity.Dogwalker;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestResponseDto {

    private Long dogwalkerId;
    private String nick;
    private int testScore; // 점수
    private LocalDateTime testStartAt; // 시작일시
    private LocalDateTime testEndAt; // 종료일시







}
