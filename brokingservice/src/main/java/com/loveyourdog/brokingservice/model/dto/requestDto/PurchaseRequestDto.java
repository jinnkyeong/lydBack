package com.loveyourdog.brokingservice.model.dto.requestDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseRequestDto {

//    private Long paymentId;
    private Long reservationId;
    private Long paymentTypeId;
    //주문자
    private String buyer_tel;
    private String buyer_email;
    private String buyer_name;
    // 총결제금액
    private BigDecimal amount;
    // iamport
    private String pg;
    private String pay_method;
    private String merchant_uid;
    private String request_id;
}
