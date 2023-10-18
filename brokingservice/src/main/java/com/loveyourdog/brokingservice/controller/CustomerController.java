package com.loveyourdog.brokingservice.controller;

import com.loveyourdog.brokingservice.model.dto.querydsl.ApplicationDto;
import com.loveyourdog.brokingservice.model.dto.querydsl.CommisionDto;
import com.loveyourdog.brokingservice.model.dto.querydsl.CusRequireDto;
import com.loveyourdog.brokingservice.model.dto.requestDto.ApplyModRequestDto;
import com.loveyourdog.brokingservice.model.dto.requestDto.CommisionRequestDto;
import com.loveyourdog.brokingservice.model.dto.responseDto.ApplyResponseDto;
import com.loveyourdog.brokingservice.model.dto.responseDto.CommisionResponseDto;
import com.loveyourdog.brokingservice.model.dto.responseDto.ProfileResponseDto;
import com.loveyourdog.brokingservice.repository.application.ApplicationSearchCondition;
import com.loveyourdog.brokingservice.repository.commision.CommisionSearchCondition;
import com.loveyourdog.brokingservice.service.CustomerService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;

@Api(tags = "고객 컨트롤러")
@RestController
@RequiredArgsConstructor
@RequestMapping(value="/api")
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/open/customers/{cusId}")
    public ResponseEntity<ProfileResponseDto> getCustomer(@PathVariable("cusId")Long cusId){
        return new ResponseEntity<>(customerService.getCustomer(cusId),HttpStatus.OK);
    }


    // info -> commision 생성
    @PostMapping("/commisions")
    public ResponseEntity<Boolean> createCommision(@RequestBody CommisionRequestDto requestDto) throws AccountNotFoundException {
        return new ResponseEntity<>(customerService.createCommision(requestDto), HttpStatus.OK);
    }
    // () -> commision list
    @GetMapping("/open/commisions")
    public ResponseEntity<List<CommisionResponseDto>> getEveryCommisions(){
        return new ResponseEntity<>(customerService.getEveryCommisions(), HttpStatus.OK);
    }
    // id -> commision
    @GetMapping("/open/commisions/{id}")
    public ResponseEntity<CommisionResponseDto> getCommision(@PathVariable("id")Long id){
        return new ResponseEntity<>(customerService.getCommision(id),HttpStatus.OK);
    }

    // customer id -> commision list
    @GetMapping("/open/commisions/cus/{cusId}")
    public ResponseEntity<List<CommisionResponseDto>> getCommisionsByCusId(@PathVariable("cusId")Long cusId){
        return new ResponseEntity<>(customerService.getCommisionsByCusId(cusId),HttpStatus.OK);
    }

    // id,info -> commision 수정
    @PostMapping("/commisions/mod/{id}")
    public ResponseEntity<Boolean> modifyCommision(@PathVariable("id")Long id,
                                                     @RequestBody CommisionRequestDto requestDto) throws Exception {
        return new ResponseEntity<>(customerService.modifyCommision(id, requestDto),HttpStatus.OK);
    }
    // id -> commision 삭제
    @GetMapping("/commisions/del/{id}")
    public ResponseEntity<Boolean> deleteCommision(@PathVariable("id")Long id){
        return new ResponseEntity<>(customerService.deleteCommision(id),HttpStatus.OK);
    }


    // conditions -> commision list
    @PostMapping("/open/commisions/condition")
    public ResponseEntity<Page<CommisionDto>> filterWithConditions(
//            @RequestParam(value = "page", required = false, defaultValue = "1")int page,
//            @RequestParam(value = "size", required = false, defaultValue = "5") int size,
            @RequestBody CommisionSearchCondition condition
//            @RequestParam(value = "orderStr", required = false) List<String> orderStr

    ){
        Page<CommisionDto> dtos = customerService.filterWithConditions(condition);
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping("/open/commisions/add/view/{comId}")
    public ResponseEntity<Boolean> addComViewCnt(@PathVariable("comId")Long comId){
        return new ResponseEntity<>(customerService.addComViewCnt(comId),HttpStatus.OK);
    }







}
