package com.loveyourdog.brokingservice.controller;

import com.loveyourdog.brokingservice.model.dto.requestDto.InquiryRequestDto;
import com.loveyourdog.brokingservice.model.dto.responseDto.InquiryResponseDto;
import com.loveyourdog.brokingservice.repository.inquiry.InquiryCondition;
import com.loveyourdog.brokingservice.service.InquiryService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "(고객의)문의 컨트롤러")
@RestController
@RequiredArgsConstructor
@RequestMapping(value="/api")
public class InquiryController {


    private final InquiryService inquiryService;

    // 문의하기
    @PostMapping("/inquiries")
    public ResponseEntity<Boolean> createInquiry(@RequestBody InquiryRequestDto requestDto){
        return new ResponseEntity<>(inquiryService.createInquiry(requestDto), HttpStatus.OK);
    }

    // 고객이 한 문의 찾기 / 도그워커가 받은 문의 찾기
    // userId, userType : 찾는 주체인 도그워커 또는 고객
    // key : 1,4 - alive, 2 - canceled
    @PostMapping("/open/inquiries/search")
    public ResponseEntity<Page<InquiryResponseDto>> getInquiriesById(@RequestBody InquiryCondition condition) {
        return new ResponseEntity<>(inquiryService.getInquiriesByUserId(condition), HttpStatus.OK);
    }

    @GetMapping("/inquiries/invalid/{inquiryId}/{status}")
    public ResponseEntity<Boolean> invalidateInquiry(@PathVariable("inquiryId") Long inquiryId,
                                                     @PathVariable("status") int status) {
        return new ResponseEntity<>(inquiryService.invalidateInquiry(inquiryId,status), HttpStatus.OK);
    }





}
