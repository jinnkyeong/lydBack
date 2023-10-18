package com.loveyourdog.brokingservice.repository.inquiry;



import com.loveyourdog.brokingservice.model.entity.Application;
import com.loveyourdog.brokingservice.model.entity.Inquiry;
import com.loveyourdog.brokingservice.repository.application.ApplicationSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

//@Repository
public interface InquiryRepositoryCustom {
    public Page<Inquiry> search(InquiryCondition condition, PageRequest page);
}

