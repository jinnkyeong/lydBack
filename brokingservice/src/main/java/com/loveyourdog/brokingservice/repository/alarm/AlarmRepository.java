package com.loveyourdog.brokingservice.repository.alarm;

import com.loveyourdog.brokingservice.model.entity.Alarm;
import com.loveyourdog.brokingservice.model.entity.Application;
import com.loveyourdog.brokingservice.repository.application.ApplicationRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository extends JpaRepository<Alarm,Long> {
}
