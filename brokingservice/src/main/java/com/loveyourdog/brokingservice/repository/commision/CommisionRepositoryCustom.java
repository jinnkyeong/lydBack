package com.loveyourdog.brokingservice.repository.commision;



import com.loveyourdog.brokingservice.model.entity.Application;
import com.loveyourdog.brokingservice.model.entity.Commision;
import com.loveyourdog.brokingservice.repository.application.ApplicationSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

//@Repository
public interface CommisionRepositoryCustom {
    public Page<Commision> search(CommisionSearchCondition condition, PageRequest page);
}
