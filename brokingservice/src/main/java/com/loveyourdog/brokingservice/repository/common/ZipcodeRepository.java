package com.loveyourdog.brokingservice.repository.common;

import com.loveyourdog.brokingservice.model.entity.A;
import com.loveyourdog.brokingservice.model.entity.Zipcode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ZipcodeRepository extends JpaRepository<Zipcode,Long> {
}
