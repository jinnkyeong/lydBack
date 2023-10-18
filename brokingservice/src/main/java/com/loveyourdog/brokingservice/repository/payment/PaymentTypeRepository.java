package com.loveyourdog.brokingservice.repository.payment;

import com.loveyourdog.brokingservice.model.entity.Offer;
import com.loveyourdog.brokingservice.model.entity.PaymentType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentTypeRepository extends JpaRepository<PaymentType,Long> {


}
