package com.loveyourdog.brokingservice.controller;

import com.loveyourdog.brokingservice.model.dto.querydsl.CusRequireDto;
import com.loveyourdog.brokingservice.model.dto.requestDto.ImageRequestDto;
import com.loveyourdog.brokingservice.model.dto.requestDto.ReservationRequestDto;
import com.loveyourdog.brokingservice.model.dto.requestDto.ReviewRequestDto;
import com.loveyourdog.brokingservice.model.dto.responseDto.BasicRequireResponseDto;
import com.loveyourdog.brokingservice.model.dto.responseDto.ReservationResponseDto;
import com.loveyourdog.brokingservice.model.dto.responseDto.ReviewResponseDto;
import com.loveyourdog.brokingservice.service.ReservationService;
import com.loveyourdog.brokingservice.service.ReviewService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/api")
public class ReviewController {


    private final ReviewService reviewService;


    // info -> review 생성
    @PostMapping(value = "/reviews", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Long> createReview(
            @RequestPart(value="image", required=false) List<MultipartFile> files,
            @RequestPart(value = "requestDto")ReviewRequestDto requestDto
    ) throws Exception {
        return new ResponseEntity<>(reviewService.createReview(files, requestDto), HttpStatus.OK);
    }

    // dogwalker id -> review list
    @GetMapping("/open/reviews/dw/{dwId}")
    public ResponseEntity<List<ReviewResponseDto>> getReviewsByDwId(@PathVariable("dwId")Long dwId){
        return new ResponseEntity<>(reviewService.getReviewsByDwId(dwId), HttpStatus.OK);
    }

    // customer id -> review list
    @GetMapping("/open/reviews/cus/{cusId}")
    public ResponseEntity<List<ReviewResponseDto>> getReviewsByCusId(@PathVariable("cusId")Long cusId){
        return new ResponseEntity<>(reviewService.getReviewsByCusId(cusId), HttpStatus.OK);
    }

    // 메인화면에 보여줄 3개 리뷰(4.5이상, 최신순)
    @GetMapping("/open/reviews/good")
    public ResponseEntity<List<ReviewResponseDto>> getGoodReviews(){
        return new ResponseEntity<>(reviewService.getGoodReviews(), HttpStatus.OK);
    }

    // id -> review
    @GetMapping("/open/reviews/{id}")
    public ResponseEntity<ReviewResponseDto> getReviewById(@PathVariable("id")Long id) throws Exception {
        return new ResponseEntity<>(reviewService.getReviewById(id), HttpStatus.OK);
    }



}
