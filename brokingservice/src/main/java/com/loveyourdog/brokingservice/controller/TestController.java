package com.loveyourdog.brokingservice.controller;

import com.loveyourdog.brokingservice.model.dto.requestDto.QuestionRequestDto;
import com.loveyourdog.brokingservice.model.dto.responseDto.LectureResponseDto;
import com.loveyourdog.brokingservice.model.dto.responseDto.QuestionTypeResponseDto;
import com.loveyourdog.brokingservice.service.LectureService;
import com.loveyourdog.brokingservice.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/api")
public class TestController {


    private final TestService testService;



    // 테스트 문제(전부 출력)
    // () -> question type list
    @GetMapping("/questionTypes")
    public ResponseEntity<List<QuestionTypeResponseDto>> getQuestionTypes(){
        return new ResponseEntity<>(testService.getQuestionTypes(), HttpStatus.OK);
    }

    @PostMapping("/questionTypes/{dwId}/{testStartAt}/{testEndAt}")
    public ResponseEntity<Boolean> postTest(@PathVariable Long dwId,
                                            @PathVariable String testStartAt,
                                            @PathVariable String testEndAt,
                                            @RequestBody List<QuestionRequestDto> requestDtos) throws Exception {
        return new ResponseEntity<>(testService.createTest(dwId, testStartAt, testEndAt, requestDtos), HttpStatus.OK);
    }



}
