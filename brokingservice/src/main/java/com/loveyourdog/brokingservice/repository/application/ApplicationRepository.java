package com.loveyourdog.brokingservice.repository.application;

import com.loveyourdog.brokingservice.model.entity.Application;
import com.loveyourdog.brokingservice.model.entity.Dogwalker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application,Long>, ApplicationRepositoryCustom {
}
