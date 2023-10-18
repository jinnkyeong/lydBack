package com.loveyourdog.brokingservice.repository.offer;



import com.loveyourdog.brokingservice.model.entity.Inquiry;
import com.loveyourdog.brokingservice.model.entity.Offer;
import com.loveyourdog.brokingservice.repository.inquiry.InquiryCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

//@Repository
public interface OfferRepositoryCustom {
    public Page<Offer> search(InquiryCondition condition, PageRequest page);
}

