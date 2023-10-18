package com.loveyourdog.brokingservice.service;

import com.loveyourdog.brokingservice.model.dto.requestDto.OfferRequestDto;
import com.loveyourdog.brokingservice.model.dto.responseDto.InquiryResponseDto;
import com.loveyourdog.brokingservice.model.dto.responseDto.OfferResponseDto;
import com.loveyourdog.brokingservice.model.entity.*;
import com.loveyourdog.brokingservice.repository.application.ApplicationOrderCondition;
import com.loveyourdog.brokingservice.repository.application.ApplicationRepository;
import com.loveyourdog.brokingservice.repository.commision.CommisionOrderCondition;
import com.loveyourdog.brokingservice.repository.commision.CommisionRepository;
import com.loveyourdog.brokingservice.repository.customer.CustomerRespository;
import com.loveyourdog.brokingservice.repository.dogwalker.DogwalkerRepository;
import com.loveyourdog.brokingservice.repository.inquiry.InquiryCondition;
import com.loveyourdog.brokingservice.repository.offer.OfferRepository;
import com.loveyourdog.brokingservice.repository.offer.OfferRepositoryImpl;
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
public class OfferService {


    private final OfferRepository offerRepository;
    private final ApplicationRepository applicationRepository;
    private final CommisionRepository commisionRepository;
    private final DogwalkerRepository dogwalkerRepository;
    private final CustomerRespository customerRespository;
    private final OfferRepositoryImpl offerRepositoryImpl;



    public boolean createOffer(OfferRequestDto requestDto){
        Application application = applicationRepository.findById(requestDto.getApplicationId()).orElseThrow(
                ()-> new NoSuchElementException("해당 id와 일치하는 지원서가 없습니다"));
        Commision commision = commisionRepository.findById(requestDto.getCommisionId()).orElseThrow(
                ()-> new NoSuchElementException("해당 id와 일치하는 의뢰서가 없습니다"));
        Offer offer = Offer.builder()
                .status(requestDto.getStatus())
                .application(application)
                .commision(commision)
                .dogwalker(application.getDogwalker())
                .customer(commision.getCustomer())
                .build();
        // 변경 수락의 경우 -> 가격 입력안받음 -> 변경된 지원서로 price 바꾸기
        if(requestDto.getStatus()==4){
            offer.setPrice(application.getPrice());
        } else {
            offer.setPrice(requestDto.getPrice());
        }
        offerRepository.save(offer);
        return true;
    }


    // key 1: alive(1,4)  2: canceled(0)  3: rejected(3)
    public List<OfferResponseDto> getOfferById(Long userId, String userType, int key){
        List<Offer> offers = new ArrayList<>();
        List<OfferResponseDto> dtos = new ArrayList<>();

        if(userType.equalsIgnoreCase("dogwalker")) {

            Dogwalker dogwalker = dogwalkerRepository.findById(userId).orElseThrow(
                    ()-> new NoSuchElementException("해당 id와 일치하는 dw가 없습니다"));

            for (Application app :dogwalker.getApplications()) {
                for (Offer o :app.getOffers()) {
                    if(key==1){
                        if(o.getStatus()==1 || o.getStatus()==4){
                            offers.add(o);
                        }
                    } else if (key==2) {
                        if(o.getStatus()==0 ){
                            offers.add(o);
                        }
                    } else {
                        if(o.getStatus()==3){
                            offers.add(o);
                        }
                    }

                }
            }


        } else if(userType.equalsIgnoreCase("customer")) {

            Customer customer = customerRespository.findById(userId).orElseThrow(
                    () -> new NoSuchElementException("해당 id와 일치하는 cus가 없습니다"));
            for (Commision com:customer.getCommisions()) {
                for (Offer o : com.getOffers()) {
                    if(key==1){
                        if(o.getStatus()==1 || o.getStatus()==4){
                            offers.add(o);
                        }
                    } else if (key==2) {
                        if(o.getStatus()==0 ){
                            offers.add(o);
                        }
                    } else {
                        if(o.getStatus()==3){
                            offers.add(o);
                        }
                    }

                }
            }

        }
        // dto변환
        System.out.println("offers.size()"+offers.size());
        for (Offer o:offers) {
            dtos.add(o.toOfferResponseDto());
        }
        return dtos;

    }


    public Page<OfferResponseDto> getOffersByUserId(InquiryCondition condition) {
        ApplicationOrderCondition orderCondition = new ApplicationOrderCondition();
        List<Sort.Order> orders = new ArrayList<>();
        if (condition.getOrderStr()!=null && condition.getOrderStr().size()>0) {
            for (String str:condition.getOrderStr()) {
                String[] value = str.split("/");
                orderCondition.setDirection(value[0]); // null 경우 DESC 반환
                orderCondition.setProperties(value[1]); // null 경우 view 반환
                orders.add(new Sort.Order(Sort.Direction.fromString(orderCondition.getDirection()),orderCondition.getProperties()));
            }
        }
        return offerRepositoryImpl.search(
                        condition,
                        PageRequest.of( // PageRequest 세팅
                                condition.getPage()-1,
                                condition.getSize(),
                                Sort.by(orders)
                        ))
                .map(Offer::toOfferResponseDto);
    }

        public boolean invalidateOffer(Long ofrId, int status){
        Offer offer = offerRepository.findById(ofrId).orElseThrow(
                ()-> new NoSuchElementException("해당 id와 일치하는 inquiry가 없습니다"));
        offer.setStatus(status);      // 0: 취소  // 3: 거절 // 5: 상대가 변경수락해서 무용해진 offer
        offer.setInvalidatedAt(LocalDateTime.now());
        offerRepository.save(offer);
        return true;
    }
}
