package com.loveyourdog.brokingservice.repository.application;



import com.loveyourdog.brokingservice.model.dto.querydsl.ApplicationDto;
import com.loveyourdog.brokingservice.model.dto.querydsl.DogwalkerDto;
import com.loveyourdog.brokingservice.model.entity.Application;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

//@Repository
public interface ApplicationRepositoryCustom {
//    public Page<Application> search(ApplicationSearchCondition condition, Pageable pageable);}
    public Page<Application> search(ApplicationSearchCondition condition, PageRequest page);}
