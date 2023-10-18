package com.loveyourdog.brokingservice.repository.certificate;

import com.loveyourdog.brokingservice.model.entity.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CertificateRespository extends JpaRepository<Certificate,Long> {
    Optional<Certificate> findByKeyword(String keyword);
}