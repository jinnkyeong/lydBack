package com.loveyourdog.brokingservice.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class IamPort {
    @Value("${iamport.service-code}")
    private String serviceCode;

    @Value("${iamport.api.key}")
    private String apiKey;

    @Value("${iamport.api.secretKey}")
    private String secretKey;

}
