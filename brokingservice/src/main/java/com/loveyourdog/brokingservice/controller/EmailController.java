package com.loveyourdog.brokingservice.controller;

import com.loveyourdog.brokingservice.model.dto.requestDto.EmailResponseDto;
import com.loveyourdog.brokingservice.security.UserTypeDto;
import com.loveyourdog.brokingservice.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value="/api")
public class EmailController {

    @Autowired
    private EmailService emailService;

    // 임시 비밀번호 발급
    @PostMapping("/open/send-mail/pwd")
    public ResponseEntity<Boolean> sendPasswordMail(@RequestBody UserTypeDto userTypeDto) throws Exception {
        return new ResponseEntity<>(emailService.sendPasswordMail(userTypeDto),HttpStatus.OK);
    }

    // 회원가입 이메일 인증 - 요청 시 body로 인증번호 반환하도록 작성하였음
    @PostMapping("/open/send-mail/email")
    public ResponseEntity<EmailResponseDto> sendJoinMail(@RequestBody UserTypeDto userTypeDto) {
        return new ResponseEntity<>(emailService.sendJoinMail(userTypeDto), HttpStatus.OK);
    }

}
