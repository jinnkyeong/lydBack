package com.loveyourdog.brokingservice.model.auth;

import lombok.Getter;

@Getter
public class KakaoRequestDto {
    private String code;
    private String userType;
}
