package com.loveyourdog.brokingservice.controller;

import com.loveyourdog.brokingservice.model.dto.requestDto.QuestionRequestDto;
import com.loveyourdog.brokingservice.model.dto.responseDto.FaqResponseDto;
import com.loveyourdog.brokingservice.model.dto.responseDto.QuestionTypeResponseDto;
import com.loveyourdog.brokingservice.service.FaqService;
import com.loveyourdog.brokingservice.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/api")
public class FaqController {


    private final FaqService faqService;


    @GetMapping("/open/faqs/{cate}")
    public ResponseEntity<List<FaqResponseDto>> getFaqs(@PathVariable("cate") int cate){
        return new ResponseEntity<>(faqService.getFaqs(cate), HttpStatus.OK);
    }

    @GetMapping("/open/faqs/cates")
    public ResponseEntity<List<Integer>> getFaqCates(){
        return new ResponseEntity<>(faqService.getFaqCates(), HttpStatus.OK);
    }





}
