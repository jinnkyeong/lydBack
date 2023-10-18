package com.loveyourdog.brokingservice.repository.test;

import com.loveyourdog.brokingservice.model.entity.QuestionType;
import com.loveyourdog.brokingservice.model.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionTypeRepository extends JpaRepository<QuestionType,Long> {


}
