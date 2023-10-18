package com.loveyourdog.brokingservice.repository.test;

import com.loveyourdog.brokingservice.model.entity.Question;
import com.loveyourdog.brokingservice.model.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question,Long> {


}
