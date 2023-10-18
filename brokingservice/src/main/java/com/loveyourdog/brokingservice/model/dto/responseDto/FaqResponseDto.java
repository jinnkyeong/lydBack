package com.loveyourdog.brokingservice.model.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FaqResponseDto {
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

}
