package com.loveyourdog.brokingservice.repository.payment;

import com.loveyourdog.brokingservice.model.entity.Offer;
import com.loveyourdog.brokingservice.model.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment,Long> {


}
