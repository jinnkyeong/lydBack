package com.loveyourdog.brokingservice.model.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OAuthToken {
//        response.getBody() :
//        {"access_token":"NQk4L0m3NoVHV8gjDeQuYhFxVO5aVCPFaZpVHr8cCiolTgAAAYrmpBWB",
//        "token_type":"bearer",
//        "refresh_token":"QTBjjxXbNGLU3NA7vwBBY4Mi6Tpmy3xeAXMYS5SBCiolTgAAAYrmpBWA",
//        "expires_in":21599,
//        "scope":"age_range account_email profile_image gender profile_nickname",
//        "refresh_token_expires_in":5183999}
    private String access_token;
}
