package com.loveyourdog.brokingservice.controller;

import com.loveyourdog.brokingservice.model.auth.KakaoProfile;
import com.loveyourdog.brokingservice.model.auth.KakaoRequestDto;
import com.loveyourdog.brokingservice.model.auth.OAuthToken;
import com.loveyourdog.brokingservice.repository.customer.CustomerRespository;
import com.loveyourdog.brokingservice.repository.dogwalker.DogwalkerRepository;
import com.loveyourdog.brokingservice.security.SignRequest;
import com.loveyourdog.brokingservice.security.SignResponse;
import com.loveyourdog.brokingservice.security.TokenRequest;
import com.loveyourdog.brokingservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/api")
public class AuthController {

    private final AuthService authService;

    // return : 카카오서버에 code을 요청할 url
    @GetMapping(value = "/login/kakao")
    @ResponseBody
    public String getKakaoUrl() throws Exception {
        return authService.getKakaoUrl();
    }

    // data : code
    //
    @PostMapping(value="/auth/kakao")
    public ResponseEntity<SignResponse> kakaoCallback(@RequestBody KakaoRequestDto requestDto) throws Exception {
        return new ResponseEntity<>(authService.kakaoLogin(requestDto),HttpStatus.OK);
    }
    @PostMapping("/auth/kakao/userId")
    public ResponseEntity<SignResponse> findUserId(@RequestBody TokenRequest tokenRequest) throws Exception {
        return new ResponseEntity<>(authService.findUserId(tokenRequest),HttpStatus.OK);
    }
}
