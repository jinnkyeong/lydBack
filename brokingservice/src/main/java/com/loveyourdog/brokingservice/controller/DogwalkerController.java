package com.loveyourdog.brokingservice.controller;

import com.loveyourdog.brokingservice.model.dto.querydsl.ApplicationDto;
import com.loveyourdog.brokingservice.model.dto.requestDto.ApplyModRequestDto;
import com.loveyourdog.brokingservice.model.dto.requestDto.ApplyRequestDto;
import com.loveyourdog.brokingservice.model.dto.requestDto.CertificateRequestDto;
import com.loveyourdog.brokingservice.model.dto.querydsl.DogwalkerDto;
import com.loveyourdog.brokingservice.model.dto.responseDto.ApplyDetailResponseDto;
import com.loveyourdog.brokingservice.model.dto.responseDto.ApplyResponseDto;
import com.loveyourdog.brokingservice.model.dto.responseDto.ProfileResponseDto;
import com.loveyourdog.brokingservice.model.dto.responseDto.TestResponseDto;
import com.loveyourdog.brokingservice.model.entity.Application;
import com.loveyourdog.brokingservice.repository.application.ApplicationOrderCondition;
import com.loveyourdog.brokingservice.repository.application.ApplicationSearchCondition;
import com.loveyourdog.brokingservice.service.DogwalkerService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;

@Api(tags = "도그워커 컨트롤러")
@RestController
@RequiredArgsConstructor
@RequestMapping(value="/api")
public class DogwalkerController {

    private final DogwalkerService dogwalkerService;


    // dogwalker id -> dogwalker
    @GetMapping("/open/dogwalkers/{dwId}")
    public ResponseEntity<ProfileResponseDto> getDogwalker(@PathVariable("dwId")Long dwId){
        return new ResponseEntity<>(dogwalkerService.getDogwalker(dwId),HttpStatus.OK);
    }


    // dogwalker id -> dogwalker 중 test 정보
    @GetMapping("/tests/{dwId}")
    public ResponseEntity<TestResponseDto> getTestByDwId(@PathVariable("dwId")Long dwId){
        return new ResponseEntity<>(dogwalkerService.getTestByDwId(dwId),HttpStatus.OK);
    }


    // info -> application 생성
    @PostMapping("/applications")
    public ResponseEntity<ApplicationDto> createApplication(@RequestBody ApplyRequestDto requestDto) throws AccountNotFoundException {
        return new ResponseEntity<>(dogwalkerService.createApplication(requestDto), HttpStatus.OK);

    }

    // () -> application list
    @GetMapping("/open/applications")
    public ResponseEntity<List<ApplyResponseDto>> getEveryApplication(){
        return new ResponseEntity<>(dogwalkerService.getEveryApplication(),HttpStatus.OK);
    }

    // dogwalker id -> application list
    @GetMapping("/open/applications/dw/{dwId}")
    public ResponseEntity<List<ApplyResponseDto>> getApplicationsByDwId(@PathVariable("dwId") Long dwId){
        return new ResponseEntity<>(dogwalkerService.getApplicationsByDwId(dwId),HttpStatus.OK);
    }

    // application id -> application
    @GetMapping("/open/applications/{id}")
    public ResponseEntity<ApplyDetailResponseDto> getApplication(@PathVariable("id")Long id){
        return new ResponseEntity<>(dogwalkerService.getApplication(id),HttpStatus.OK);
    }

    // id,info -> application 수정
    @PostMapping("/applications/mod/{id}")
    public ResponseEntity<Boolean> modifyApplication(@PathVariable("id")Long id,
                                                     @RequestBody ApplyModRequestDto requestDto) throws Exception {
        return new ResponseEntity<>(dogwalkerService.modifyApplication(id, requestDto),HttpStatus.OK);
    }
    // id -> application 삭제
    @GetMapping("/applications/del/{id}")
    public ResponseEntity<Boolean> deleteApplication(@PathVariable("id")Long id){
        return new ResponseEntity<>(dogwalkerService.deleteApplication(id),HttpStatus.OK);
    }


    // conditions -> application list
    @PostMapping("/open/applications/condition")
    public ResponseEntity<Page<ApplicationDto>> filterWithConditions(@RequestBody ApplicationSearchCondition condition){
        Page<ApplicationDto> dtos = dogwalkerService.filterWithConditions(condition);
        return new ResponseEntity<>(dtos,HttpStatus.OK);
    }
    @GetMapping("/open/applications/add/view/{appId}")
    public ResponseEntity<Boolean> addAppViewCnt(@PathVariable("appId")Long appId){
        return new ResponseEntity<>(dogwalkerService.addAppViewCnt(appId),HttpStatus.OK);
    }

    @GetMapping("/applications/popular/{cusId}")
    public ResponseEntity<List<ApplyResponseDto>> getPopularAppsInmyTown(@PathVariable("cusId")Long cusId){
        return new ResponseEntity<>(dogwalkerService.getPopularAppsInmyTown(cusId),HttpStatus.OK);
    }


}
