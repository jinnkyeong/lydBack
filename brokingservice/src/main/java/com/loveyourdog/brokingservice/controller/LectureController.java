package com.loveyourdog.brokingservice.controller;

import com.loveyourdog.brokingservice.model.dto.requestDto.LectureRequestDto;
import com.loveyourdog.brokingservice.model.dto.requestDto.ReviewRequestDto;
import com.loveyourdog.brokingservice.model.dto.responseDto.LectureResponseDto;
import com.loveyourdog.brokingservice.model.dto.responseDto.ReviewResponseDto;
import com.loveyourdog.brokingservice.service.LectureService;
import com.loveyourdog.brokingservice.service.ReviewService;
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
public class LectureController {


    private final LectureService lectureService;




    // dogwalker id -> lecture list
    @GetMapping("/open/lectures/dw/{dwId}")
    public ResponseEntity<List<LectureResponseDto>> getLecturesByDwId(@PathVariable("dwId")Long dwId){
        return new ResponseEntity<>(lectureService.getLectureTypesByDwId(dwId), HttpStatus.OK);
    }
    @PostMapping("/open/lectures")
    public ResponseEntity<Boolean> postLecture(@RequestBody LectureRequestDto requestDto){
        return new ResponseEntity<>(lectureService.postLecture(requestDto), HttpStatus.OK);
    }



}
