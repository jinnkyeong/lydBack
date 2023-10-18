package com.loveyourdog.brokingservice.security;

import com.loveyourdog.brokingservice.model.enums.AccountType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignRequest {

    private Long id;

    @ApiModelProperty(example = "wlsrud0303@naver.com")
    private String pwd;



    @ApiModelProperty(example = "wlsrud0303@naver.com")
    private String email;
    @ApiModelProperty(example="EMAIL")
    private String accountType; // 계정 유형

    private String userType; // 고객 도그워커

}