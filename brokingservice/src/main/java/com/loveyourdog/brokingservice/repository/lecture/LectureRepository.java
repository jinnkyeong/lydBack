package com.loveyourdog.brokingservice.repository.lecture;

import com.loveyourdog.brokingservice.model.entity.Lecture;
import com.loveyourdog.brokingservice.model.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureRepository extends JpaRepository<Lecture,Long> {
}
