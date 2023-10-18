package com.loveyourdog.brokingservice.repository.location;

import com.loveyourdog.brokingservice.model.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location,Long> {
}
