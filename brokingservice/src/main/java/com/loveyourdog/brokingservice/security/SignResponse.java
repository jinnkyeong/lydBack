package com.loveyourdog.brokingservice.security;

import com.loveyourdog.brokingservice.model.dto.querydsl.AuthorityDto;
import com.loveyourdog.brokingservice.model.entity.Authority;
import com.loveyourdog.brokingservice.model.entity.Customer;
import com.loveyourdog.brokingservice.model.entity.Dogwalker;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignResponse {

    private Long id;
    private Long customerId;
    private Long dogwalkerId;
    private Long adminId;
    private String sign;
    private String name;
    private String nick;
    private String email;
    private String username;
    private String usernick;
    private int goalCnt; // 산책횟수 or 이용횟수
    private String dirName;
    private String fileName;
    private String extension;


//    @Builder.Default
    private List<AuthorityDto> roles = new ArrayList<>();

    private String accessToken;
    private String refreshToken;


}