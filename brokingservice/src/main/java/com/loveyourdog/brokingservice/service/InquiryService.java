package com.loveyourdog.brokingservice.service;

import com.loveyourdog.brokingservice.model.dto.requestDto.InquiryRequestDto;
import com.loveyourdog.brokingservice.model.dto.responseDto.InquiryResponseDto;
import com.loveyourdog.brokingservice.model.entity.*;
import com.loveyourdog.brokingservice.repository.application.ApplicationRepository;
import com.loveyourdog.brokingservice.repository.commision.CommisionOrderCondition;
import com.loveyourdog.brokingservice.repository.commision.CommisionRepository;
import com.loveyourdog.brokingservice.repository.customer.CustomerRespository;
import com.loveyourdog.brokingservice.repository.dogwalker.DogwalkerRepository;
import com.loveyourdog.brokingservice.repository.inquiry.InquiryCondition;
import com.loveyourdog.brokingservice.repository.inquiry.InquiryRepository;
import com.loveyourdog.brokingservice.repository.inquiry.InquiryRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
@Service
@Transactional
@RequiredArgsConstructor
public class InquiryService {

    private final InquiryRepository inquiryRepository;

    private final ApplicationRepository applicationRepository;
    private final CommisionRepository commisionRepository;
    private final DogwalkerRepository dogwalkerRepository;
    private final CustomerRespository customerRespository;
    private final InquiryRepositoryImpl inquiryRepositoryImpl;


    public boolean createInquiry(InquiryRequestDto requestDto){
        Application application = applicationRepository.findById(requestDto.getApplicationId()).orElseThrow(
                ()-> new NoSuchElementException("해당 id와 일치하는 지원서가 없습니다"));
        Commision commision = commisionRepository.findById(requestDto.getCommisionId()).orElseThrow(
                ()-> new NoSuchElementException("해당 id와 일치하는 의뢰서가 없습니다"));

        Inquiry inquiry = Inquiry.builder()
                .price(requestDto.getPrice())
                .status(1) // 단순 문의
                .application(application)
                .commision(commision)
                .dogwalker(application.getDogwalker())
                .customer(commision.getCustomer())
                .build();
        inquiryRepository.save(inquiry);
        return true;
    }


    public boolean invalidateInquiry(Long inqId, int status){
        Inquiry inquiry = inquiryRepository.findById(inqId).orElseThrow(
                ()-> new NoSuchElementException("해당 id와 일치하는 inquiry가 없습니다"));
        inquiry.setStatus(status); // 0: 취소  // 3: 거절 // 5: 상대가 변경수락해서 무용해진 inquiry
        inquiry.setInvalidatedAt(LocalDateTime.now());
        inquiryRepository.save(inquiry);
        return true;
    }

    public Page<InquiryResponseDto> getInquiriesByUserId(InquiryCondition condition){
        CommisionOrderCondition orderCondition = new CommisionOrderCondition();
        List<Sort.Order> orders = new ArrayList<>();
        if (condition.getOrderStr()!=null && condition.getOrderStr().size()>0) {
            for (String str:condition.getOrderStr()) {
                String[] value = str.split("/");
                orderCondition.setDirection(value[0]); // null 경우 DESC 반환
                orderCondition.setProperties(value[1]); // null 경우 view 반환
                orders.add(new Sort.Order(Sort.Direction.fromString(orderCondition.getDirection()),orderCondition.getProperties()));
            }
        }
        return inquiryRepositoryImpl.search(
                        condition,
                        PageRequest.of( // PageRequest 세팅
                                condition.getPage()-1,
                                condition.getSize(),
                                Sort.by(orders)
                        ))
                .map(Inquiry::toInquiryResponseDto);
//
//
//        List<Inquiry> inquiries = new ArrayList<>();
////        List<InquiryResponseDto> dtos = new ArrayList<>();
//        if(userType.equalsIgnoreCase("dogwalker")) {
//            Dogwalker dogwalker = dogwalkerRepository.findById(userId).orElseThrow(
//                    ()-> new NoSuchElementException("해당 id와 일치하는 도그워커가 없습니다"));
//            for (Inquiry i:dogwalker.getInquiries()) {
//                if(key==1){
//                    if(i.getStatus()==1 || i.getStatus()==4){ // status가 1,4인 경우만 조회
//                        inquiries.add(i);
//                    }
//                } else if(key==2){
//                    if(i.getStatus()==0 ){ // 취소상태
//                        inquiries.add(i);
//                    }
//                } else {
//                    if(i.getStatus()==3){ // 뭐였지
//                        inquiries.add(i);
//                    }
//                }
//
//            }
//        } else if(userType.equalsIgnoreCase("customer")){
//            Customer customer = customerRespository.findById(userId).orElseThrow(
//                    ()-> new NoSuchElementException("해당 id와 일치하는 고객이 없습니다"));
//            for (Inquiry i:customer.getInquiries()) {
//                if(key==1){
//                    if(i.getStatus()==1 || i.getStatus()==4){
//                        inquiries.add(i);
//                    }
//                } else if(key==2){
//                    if(i.getStatus()==0 ){
//                        inquiries.add(i);
//                    }
//                } else {
//                    if(i.getStatus()==3){
//                        inquiries.add(i);
//                    }
//                }
//            }
//        }
////        for (Inquiry i:inquiries) {
////            dtos.add(i.toInquiryResponseDto());
////        }
//
//        return dtos;

    }



}
