package com.loveyourdog.brokingservice.model.entity;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicUpdate
public class PaymentType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 해당 엔티티 한정 identity 설정
    private Long id;
    // 1 creditcard
    // 2 phonepay
    // 3 kakaopay

    private String name;


    // 결제수단
    @OneToMany(mappedBy = "paymentType", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Payment> payments  = new ArrayList<>();

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
        payments.forEach(o -> o.setPaymentType(this));

    }




}
