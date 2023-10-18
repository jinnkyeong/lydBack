package com.loveyourdog.brokingservice.sms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.loveyourdog.brokingservice.model.dto.querydsl.ZipcodeDto;
import com.loveyourdog.brokingservice.model.dto.requestDto.ImageRequestDto;
import com.loveyourdog.brokingservice.model.dto.requestDto.PwdRequestDto;
import com.loveyourdog.brokingservice.service.CommonService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/api")
public class SmsController {

    private final SmsService smsService;


    @PostMapping("/sms/send")
    public ResponseEntity<SmsResponseDTO> sendSms(@RequestBody MessageDTO messageDto) throws UnsupportedEncodingException, URISyntaxException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        return new ResponseEntity<>(smsService.sendSms(messageDto),HttpStatus.OK);
        //        return response;
    }



}