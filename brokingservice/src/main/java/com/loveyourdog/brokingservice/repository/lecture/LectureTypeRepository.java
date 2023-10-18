package com.loveyourdog.brokingservice.repository.lecture;

import com.loveyourdog.brokingservice.model.entity.Lecture;
import com.loveyourdog.brokingservice.model.entity.LectureType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureTypeRepository extends JpaRepository<LectureType,Long> {
}
