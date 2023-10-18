package com.loveyourdog.brokingservice.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserTypeDto {
    private String email;
    private String pwd;
    private String userType;
    private Long userId;
    private String phone;
}
