package com.loveyourdog.brokingservice.repository.offer;

import com.loveyourdog.brokingservice.model.entity.Application;
import com.loveyourdog.brokingservice.model.entity.Commision;
import com.loveyourdog.brokingservice.model.entity.Offer;
import com.loveyourdog.brokingservice.repository.application.ApplicationRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OfferRepository extends JpaRepository<Offer,Long> {


}
