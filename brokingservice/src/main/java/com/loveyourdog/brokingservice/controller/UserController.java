package com.loveyourdog.brokingservice.controller;

import com.loveyourdog.brokingservice.model.dto.requestDto.ReissueRequestDto;
import com.loveyourdog.brokingservice.model.entity.Customer;
import com.loveyourdog.brokingservice.model.entity.Dogwalker;
import com.loveyourdog.brokingservice.security.*;
import com.loveyourdog.brokingservice.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "로그인 기능 컨트롤러") // Swagger에 표시되는 컨트롤러명 (가독성 유념)
@RestController
@RequiredArgsConstructor
@RequestMapping(value="/api")
public class UserController {

    private final UserService userService;
    private final JwtProvider jwtProvider;

    @PostMapping(value = "/login")
    public ResponseEntity<SignResponse> login(@RequestBody SignRequest request) throws Exception {
        return new ResponseEntity<>(userService.login(request), HttpStatus.OK);
    }

    @PostMapping(value = "/register")
    public ResponseEntity<Boolean> register(@RequestBody SignRequest request) throws Exception {
        return new ResponseEntity<>(userService.register(request), HttpStatus.OK);
    }


    // atk 재발급
    @PostMapping("/auth/reissue")
    public TokenResponse dogwalkerReissue(
            @RequestBody ReissueRequestDto requestDto
    ) throws Exception {
//        AccountResponse accountResponse = null;

//
//        if(requestDto.getUserType().equalsIgnoreCase("dogwalker"){
//            DogwalkerDetails dogwalkerDetails = (DogwalkerDetails)principal;
//            Dogwalker dogwalker = dogwalkerDetails.getDogwalker(); // 현재 유저
//            accountResponse = AccountResponse.dogWalkerToAccountRes(dogwalker); // Dto에 옮겨담기
//
//        } else if(principal.toString().contains("Customer") || principal.toString().contains("customer")){
//            CustomerDetails customerDetails = (CustomerDetails)principal;
//            Customer customer = customerDetails.getCustomer();
//            accountResponse = AccountResponse.customerToAccountRes(customer);
//        } else {
//            throw new Exception();
//        }
        System.out.println("토큰 재발급 시작!");
//        return jwtProvider.reissueAtk(requestDto);
        return jwtProvider.reissueAtk(requestDto);
    }


    @PostMapping(value = "/auth/logout")
    public ResponseEntity<AccountResponse> logout(
            HttpServletRequest request
    ) throws Exception {
        // client로부터 atk 받아서 로그아웃
        System.out.println("logout 컨트롤러에 들어옴");
        return new ResponseEntity<>(userService.logout(request), HttpStatus.OK);
    }


    // 이메일 중복확인
    // email,userType -> 일치하는 유저 확인 -> boolean
    @Operation(summary = "이메일 중복확인")
    @PostMapping("/auth/checkDuplEmail")
    public ResponseEntity<Boolean> checkDuplicate(@RequestBody UserTypeDto userTypeDto) {
        return new ResponseEntity<>(userService.checkDuplEmail(userTypeDto), HttpStatus.OK);
    }

    @PostMapping("/pwd/mod/chk")
    public ResponseEntity<Boolean> checkPwd(@RequestBody UserTypeDto userTypeDto) {
        return new ResponseEntity<>(userService.checkPwd(userTypeDto), HttpStatus.OK);
    }

    @PostMapping("/pwd/mod/mod")
    public ResponseEntity<Boolean> changePwd(@RequestBody UserTypeDto userTypeDto) {
        return new ResponseEntity<>(userService.changePwd(userTypeDto), HttpStatus.OK);
    }


    @PostMapping("/phone/mod")
    public ResponseEntity<Boolean> changePhone(@RequestBody UserTypeDto userTypeDto) {
        return new ResponseEntity<>(userService.changePhone(userTypeDto), HttpStatus.OK);
    }

}
