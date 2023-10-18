package com.loveyourdog.brokingservice.controller;

import com.loveyourdog.brokingservice.model.dto.querydsl.CusRequireDto;
import com.loveyourdog.brokingservice.model.dto.requestDto.AdminSignRequestDto;
import com.loveyourdog.brokingservice.model.dto.requestDto.ReservationRequestDto;
import com.loveyourdog.brokingservice.model.dto.responseDto.BasicRequireResponseDto;
import com.loveyourdog.brokingservice.model.dto.responseDto.DogwalkerResponseDto;
import com.loveyourdog.brokingservice.model.dto.responseDto.ReservationResponseDto;
import com.loveyourdog.brokingservice.repository.dogwalker.DogwalkerRepository;
import com.loveyourdog.brokingservice.security.SignRequest;
import com.loveyourdog.brokingservice.security.SignResponse;
import com.loveyourdog.brokingservice.service.AdminService;
import com.loveyourdog.brokingservice.service.ReservationService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/api")
public class AdminController {


    private final AdminService adminService;

    @PostMapping(value = "/admin-login")
    public ResponseEntity<SignResponse> login(@RequestBody AdminSignRequestDto requestDto) throws Exception {
        return new ResponseEntity<>(adminService.login(requestDto), HttpStatus.OK);
    }




    @GetMapping("/open/dogwalkers")
    public ResponseEntity<List<DogwalkerResponseDto>> getDogwalkers() throws Exception {
        return new ResponseEntity<>(adminService.getDogwalkers(), HttpStatus.OK);
    }
    @GetMapping("/admin/dogwalkers/test")
    public ResponseEntity<List<DogwalkerResponseDto>> getDdogwalkers() throws Exception {
        return new ResponseEntity<>(adminService.getDogwalkers(), HttpStatus.OK);
    }

    @GetMapping("/admin/dogwalkers/{dwId}/{passed}")
    public ResponseEntity<Boolean> passDw(@PathVariable("dwId")Long dwId,
                                          @PathVariable("passed")int passed) throws Exception {
        return new ResponseEntity<>(adminService.passDw(dwId, passed), HttpStatus.OK);
    }
    @GetMapping("/admin/dogwalkers/appl/{dwId}/{passed}")
    public ResponseEntity<Boolean> passAppl(@PathVariable("dwId")Long dwId,
                                          @PathVariable("passed")int passed) throws Exception {
        return new ResponseEntity<>(adminService.passAppl(dwId, passed), HttpStatus.OK);
    }
    @GetMapping("/admin/dogwalkers/itv/{dwId}/{passed}")
    public ResponseEntity<Boolean> passInterview(@PathVariable("dwId")Long dwId,
                                            @PathVariable("passed")int passed) throws Exception {
        return new ResponseEntity<>(adminService.passInterview(dwId, passed), HttpStatus.OK);
    }
    @GetMapping("/admin/reservations/cal/start/{reservId}")
    public ResponseEntity<Boolean> startCalculate(
                                          @PathVariable("reservId")Long reservId) throws Exception {
        return new ResponseEntity<>(adminService.startCalculate(reservId), HttpStatus.OK);
    }

    @GetMapping("/admin/reservations/cal/end/{reservId}")
    public ResponseEntity<Boolean> endCalculate(
            @PathVariable("reservId")Long reservId) throws Exception {
        return new ResponseEntity<>(adminService.endCalculate(reservId), HttpStatus.OK);
    }

}
