package com.loveyourdog.brokingservice.model.dto.requestDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PwdRequestDto {
    private Long userId; // 유저 아이디
    private String userType; // 유저타입
    private String pwd;
}
