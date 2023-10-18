package com.loveyourdog.brokingservice.security;

import lombok.Getter;
@Getter
public class TokenRequest {

    private String accessToken;
    private String refreshToken;
    private String userType;
}
