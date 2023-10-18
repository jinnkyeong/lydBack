//package com.loveyourdog.brokingservice.oauth2;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.servlet.http.HttpServletRequest;
//
//@RestController
//@RequiredArgsConstructor
//public class Oauth2Controller {
//    private final AuthService authService;
//
//    @GetMapping("/login/kakao")
//    public ResponseEntity<LoginResponseDto> kakaoLogin(HttpServletRequest request) {
//
//        String code = request.getParameter("code"); // 인가코드
//        System.out.println(code);
//        KakaoTokenDto kakaoTokenDto = authService.getKakaoAccessToken(code);
//        System.out.println("kakaoTokenDto: " + kakaoTokenDto);
//        String kakaoAccessToken = kakaoTokenDto.getAccessToken(); // 카카오 액세스 토큰
//        System.out.println("kakaoAccessToken: " + kakaoAccessToken);
//
//        // 토큰 발급까지 authService.kakaologin 상에서 다 처리하자
//        LoginResponseDto loginResponseDto = authService.kakaoLogin(kakaoAccessToken);
//
//        return ResponseEntity.ok(loginResponseDto);
//    }
//}
