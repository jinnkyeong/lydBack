package com.loveyourdog.brokingservice.repository;

import com.loveyourdog.brokingservice.model.entity.A;
import com.loveyourdog.brokingservice.model.entity.B;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BRepository extends JpaRepository<B,Long> {
}
