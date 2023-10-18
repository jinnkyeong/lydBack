package com.loveyourdog.brokingservice.controller;

import com.loveyourdog.brokingservice.service.TestServiceTemp;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestControllerTemp {
    private final TestServiceTemp testServiceTemp;
//
//    @GetMapping("test1")
//    public ResponseEntity<Dto> dd(Long id){
//        return new ResponseEntity<>(testService.findBbyAid(id), HttpStatus.OK);
//    }
//    @GetMapping("test2")
//    public ResponseEntity<Boolean> d(Long oldId,Long newId,String name){
//        return new ResponseEntity<>(testService.updateB(oldId,newId,name), HttpStatus.OK);
//    }
    @GetMapping("test3")
    public ResponseEntity<String> ddd(Long id){
        return new ResponseEntity<>(testServiceTemp.change(id), HttpStatus.OK);
    }

}
