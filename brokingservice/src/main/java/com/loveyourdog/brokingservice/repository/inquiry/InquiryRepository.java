package com.loveyourdog.brokingservice.repository.inquiry;

import com.loveyourdog.brokingservice.model.entity.Inquiry;
import com.loveyourdog.brokingservice.model.entity.Offer;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InquiryRepository extends JpaRepository<Inquiry,Long> {


}
