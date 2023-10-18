package com.loveyourdog.brokingservice.repository.review;

import com.loveyourdog.brokingservice.model.entity.Reservation;
import com.loveyourdog.brokingservice.model.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review,Long> {


}
