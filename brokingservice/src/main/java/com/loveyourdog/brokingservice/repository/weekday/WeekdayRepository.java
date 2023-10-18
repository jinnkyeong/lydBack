package com.loveyourdog.brokingservice.repository.weekday;

import com.loveyourdog.brokingservice.model.entity.Weekday;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeekdayRepository extends JpaRepository<Weekday,Long> {
}
