package com.loveyourdog.brokingservice.repository.faq;

import com.loveyourdog.brokingservice.model.entity.Faq;
import com.loveyourdog.brokingservice.model.entity.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FaqRepository extends JpaRepository<Faq,Long> {

    Optional<List<Faq>> findByCategory(int cate);
}
