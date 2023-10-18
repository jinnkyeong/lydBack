package com.loveyourdog.brokingservice.model.entity;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicUpdate
public class Payment {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // iamport
    private String merchant_uid; // 주문정보
    private String pg;
    private String payMethod;
    private String requestId;



    //주문자
    @JoinColumn(name="customer")
    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    private String phone;
    private String email;
    private String name;
    private BigDecimal totPrice; // 총결제금액
    //결제수단
//    private String cardNumber;
//    private String cardCompany;
//    private String secretCode;
//    private String expirationMonth; // MM
//    private String expirationYear; // YY


    // 예약
    // payment(1) : reservation(1)
    @JoinColumn(name="reservation")
    @OneToOne(fetch = FetchType.LAZY)
    private Reservation reservation;



    @JoinColumn(name="paymentType")
    @ManyToOne(fetch = FetchType.LAZY)
    private PaymentType paymentType;

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }


}
