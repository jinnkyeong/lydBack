package com.loveyourdog.brokingservice.repository.reservation;

import com.loveyourdog.brokingservice.model.entity.Offer;
import com.loveyourdog.brokingservice.model.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation,Long> {


}
