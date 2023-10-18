package com.loveyourdog.brokingservice.service;


import com.loveyourdog.brokingservice.model.dto.obj.Image;
import com.loveyourdog.brokingservice.model.dto.requestDto.ReviewRequestDto;
import com.loveyourdog.brokingservice.model.dto.responseDto.FaqResponseDto;
import com.loveyourdog.brokingservice.model.dto.responseDto.ReviewResponseDto;
import com.loveyourdog.brokingservice.model.entity.*;
import com.loveyourdog.brokingservice.repository.dogwalker.DogwalkerRepository;
import com.loveyourdog.brokingservice.repository.faq.FaqRepository;
import com.loveyourdog.brokingservice.repository.reservation.ReservationRepository;
import com.loveyourdog.brokingservice.repository.review.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class FaqService {


    private final FaqRepository faqRepository;


    public List<FaqResponseDto> getFaqs(int cate){
        List<FaqResponseDto> responseDtos = new ArrayList<>();
        List<Faq> faqs = new ArrayList<>();
        if(cate<=0){
            faqs = faqRepository.findAll();
        } else {
            faqs = faqRepository.findByCategory(cate).orElseThrow(()->{
                throw new NoSuchElementException("cate "+cate+"에 해당하는 faq가 없습니다");
            });
        }
        for (Faq faq:faqs) {
            responseDtos.add(faq.toDto());
        }
        return responseDtos;
    }
    public List<Integer> getFaqCates(){
        List<Integer> cates = new ArrayList<>();
        List<Faq> faqs = faqRepository.findAll();
        for (Faq faq:faqs) {
            cates.add(faq.getCategory());
        }
        List<Integer> newCates =  cates.stream().distinct().collect(Collectors.toList()); // 중복제거
//        // 1은 기타라서 맨 뒤로 보냄
//        for (Integer n:newCates) {
//            if(n==1){
//                newCates.remove(n);
//            }
//        }
//            newCates.add(1);
        return newCates;
    }


}