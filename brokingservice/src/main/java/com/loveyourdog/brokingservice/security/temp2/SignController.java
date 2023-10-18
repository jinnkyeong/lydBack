package com.loveyourdog.brokingservice.security.temp2;//package com.loveyourdog.brokingservice.security.temp2;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.loveyourdog.brokingservice.model.entity.Customer;
//import com.loveyourdog.brokingservice.model.entity.Dogwalker;
//import com.loveyourdog.brokingservice.security.*;
//import io.swagger.annotations.Api;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.http.HttpServletRequest;
//
//@Api(tags = "sign-controller") // Swagger에 표시되는 컨트롤러명 (가독성 유념)
//
//@RestController
//@RequiredArgsConstructor
//public class SignController {
//
//    private final SignService memberService;
//    private final JwtProvider jwtProvider;
//
////    @Operation(summary = "로그인", description = "로그인로그인")
//    @PostMapping(value = "/login")
//    public ResponseEntity<SignResponse> login(@RequestBody SignRequest request) throws Exception {
//        System.out.println("로그인 컨트롤러로 들어옴");
//        System.out.println("// request에 유저 타입 넘겨줘야 함!!!!!!!");
//        return new ResponseEntity<>(memberService.login(request), HttpStatus.OK);
//    }
//
//    @PostMapping(value = "/register")
//    public ResponseEntity<Boolean> register(SignRequest request) throws Exception {
//        System.out.println("회원가입 컨트롤러로 들어옴");
//        return new ResponseEntity<>(memberService.register(request), HttpStatus.OK);
//    }
//
//    // @GetMapping("/user/get")
//    // public ResponseEntity<SignResponse> getUser(@RequestParam String account)
//    // throws Exception {
//    // return new ResponseEntity<>( memberService.getMember(account),
//    // HttpStatus.OK);
//    // }
//
//    // atk 재발급
//    @GetMapping("/auth/cus/reissue")
//    public TokenResponse customerReissue(
//            @AuthenticationPrincipal CustomerDetails customerDetails
//            // 이걸쓰려면 시큐리티를 써야함
//    ) throws JsonProcessingException {
//        System.out.println("reissue 컨트롤러에 들어옴");
//        Customer customer = customerDetails.getCustomer(); // 현재 유저
//        AccountResponse accountResponse = AccountResponse.customerToAccountRes(customer); // Dto에 옮겨담기
//        System.out.println("토큰 재발급 시작!");
//        return jwtProvider.reissueAtk(accountResponse);
//    }
//    @GetMapping("/auth/cus/reissue")
//    public TokenResponse dogwalkerReissue(
//            @AuthenticationPrincipal DogwalkerDetails dogwalkerDetails
//            // 이걸쓰려면 시큐리티를 써야함
//    ) throws JsonProcessingException {
//        System.out.println("reissue 컨트롤러에 들어옴");
//        Dogwalker dogwalker = dogwalkerDetails.getDogwalker(); // 현재 유저
//        AccountResponse accountResponse = AccountResponse.dogWalkerToAccountRes(dogwalker); // Dto에 옮겨담기
//        System.out.println("토큰 재발급 시작!");
//        return jwtProvider.reissueAtk(accountResponse);
//    }
//
//    @PostMapping(value = "/auth/logout")
//    public ResponseEntity<AccountResponse> logout(
//            //TokenDto tokenDto,
//            HttpServletRequest request
//    ) throws Exception {
//        // client로부터 atk 받아서 로그아웃
//        System.out.println("logout 컨트롤러에 들어옴");
//        //return "test";
//        return new ResponseEntity<>(memberService.logout(request), HttpStatus.OK);
//    }
//
//    @PostMapping(value = "/user/test")
//    public ResponseEntity<String> test(
//            @RequestBody String text,
//            HttpServletRequest request) {
//        System.out.println("test 컨트롤러에 들어옴");
//        System.out.println(text);
//        System.out.println("req auth : " + request.getHeader("Authorization"));
//        return new ResponseEntity<>(text, HttpStatus.OK);
//    }
//
//    @GetMapping(value = "/user/test2")
//    public ResponseEntity<String> tes2t(
//            // @RequestBody String text,
//            HttpServletRequest request
//    // @AuthenticationPrincipal CustomUserDetails customUserDetails
//    ) {
//        System.out.println("test 컨트롤러에 들어옴");
//        System.out.println("req auth : " + request.getHeader("Authorization"));
//        // System.out.println("customUserDetails name :
//        // "+customUserDetails.getUsername());
//        return new ResponseEntity<>("헤더에 토큰넣고 인증성공해서 해당컨트롤러가 요청을 받았음", HttpStatus.OK);
//    }
//
//}