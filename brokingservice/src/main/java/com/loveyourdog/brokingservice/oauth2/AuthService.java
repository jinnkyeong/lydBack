//package com.loveyourdog.brokingservice.oauth2;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.client.RestTemplate;
//
//import javax.transaction.Transactional;
//
//@Service
//@Transactional
//@RequiredArgsConstructor
//public class AuthService {
//    @Value("${spring.security,oauth2.client.registration.kakao.client-id}")
//    private static String KAKAO_CLIENT_ID;
//    @Value("${spring.security,oauth2.client.registration.kakao.redirect-uri}")
//    private static String KAKAO_REDIRECT_URI; // http://localhost:8090/oauth2/callback/kakao
//
//
//    //인가코드 -> 액세스토큰dto
//    public KakaoTokenDto getKakaoAccessToken(String code) {
//
//        RestTemplate rt = new RestTemplate(); //통신용
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
//
//        // HttpBody 객체 생성
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("grant_type", "authorization_code"); //카카오 공식문서 기준 authorization_code 로 고정
//        params.add("client_id", KAKAO_CLIENT_ID); //카카오 앱 REST API 키
//        params.add("redirect_uri", KAKAO_REDIRECT_URI);
//        params.add("code", code); //인가 코드 요청시 받은 인가 코드값, 프론트에서 받아오는 그 코드
//
//        // 헤더와 바디 합치기 위해 HttpEntity 객체 생성
//        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);
//        System.out.println(kakaoTokenRequest);
//
//        // 카카오로부터 Access token 수신
//        ResponseEntity<String> accessTokenResponse = rt.exchange(
//                "https://kauth.kakao.com/oauth/token",
//                HttpMethod.POST,
//                kakaoTokenRequest,
//                String.class
//        );
//
//        // JSON Parsing (-> KakaoTokenDto)
//        ObjectMapper objectMapper = new ObjectMapper();
//        KakaoTokenDto kakaoTokenDto = null;
//        try {
//            kakaoTokenDto = objectMapper.readValue(accessTokenResponse.getBody(), KakaoTokenDto.class);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//
//        return kakaoTokenDto;
//    }
//
//    // kakaoAccessToken 으로 카카오 로그인 서버에 정보 요청하는 메소드.
//    public KakaoAccountDto getKakaoInfo(String kakaoAccessToken) {
//
//        RestTemplate rt = new RestTemplate();
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Authorization", "Bearer " + kakaoAccessToken);
//        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
//
//        HttpEntity<MultiValueMap<String, String>> accountInfoRequest = new HttpEntity<>(headers);
//
//        // POST 방식으로 API 서버에 요청 보내고, response 받아옴
//        ResponseEntity<String> accountInfoResponse = rt.exchange(
//                "https://kapi.kakao.com/v2/user/me",
//                HttpMethod.POST,
//                accountInfoRequest,
//                String.class
//        );
//
//        System.out.println("카카오 서버에서 정상적으로 데이터를 수신했습니다.");
//        // JSON Parsing (-> kakaoAccountDto)
//        ObjectMapper objectMapper = new ObjectMapper();
//        KakaoAccountDto kakaoAccountDto = null;
//        try {
//            kakaoAccountDto = objectMapper.readValue(accountInfoResponse.getBody(), KakaoAccountDto.class);
//        } catch (JsonProcessingException e) { e.printStackTrace(); }
//
//        return kakaoAccountDto;
//
//    }
//    //kakaoAccountDto 를 Account 객체로 매핑하는 메소드.
//    public Account mapKakaoInfo(KakaoAccountDto accountDto) {
//
//        // kakaoAccountDto 에서 필요한 정보 꺼내기
//        Long kakaoId = accountDto.getId();
//        String email = accountDto.getKakao_account().getEmail();
//        String nickname = accountDto.getKakao_account().getProfile().getNickname();
//
//        System.out.println("매핑한 정보 : " + email + ", " + nickname);
//        // Account 객체에 매핑
//        return Account.builder()
//                .socialId(kakaoId)
//                .email(email)
//                .nickname(nickname)
//                .role("USER")
//                .build();
//    }
//    // login 요청 보내는 회원가입 유무 판단해 분기 처리할 메소드.
//
//    // 액세스토큰 -> 회원정보 얻어서
//    public LoginResponseDto kakaoLogin(String kakaoAccessToken) {
//
//        // 액세스토큰 -> 카카오 회원정보
//        KakaoAccountDto kakaoAccountDto = getKakaoInfo(kakaoAccessToken);
//        String kakaoEmail = kakaoAccountDto.getKakao_account().getEmail();
//
//        // kakaoAccountDto -> Account
//        Account selectedAccount = mapKakaoInfo(kakaoAccountDto);
//        System.out.println("수신된 account 정보 : " + selectedAccount);
//
//        // 매핑만 하고 DB에 저장하질 않았으니까 Autogenerated 인 id가 null 로 나왔던거네 아... 오케오케 굿
//
//        LoginResponseDto loginResponseDto = new LoginResponseDto();
//        loginResponseDto.setKakaoAccessToken(kakaoAccessToken);
//        // 가입되어 있으면 해당하는 Account 객체를 응답
//        // 존재하면 true + access token 을, 존재하지 않으면 False 리턴
//        if (accountRepository.existsByEmail(kakaoEmail)) {
//            Account resultAccount = accountRepository.findByEmail(kakaoEmail);
//            loginResponseDto.setLoginSuccess(true);
//            loginResponseDto.setKakaoAccessToken(kakaoAccessToken);
//            loginResponseDto.setAccount(resultAccount);
//            System.out.println("response setting: " + loginResponseDto);
//
//            // 토큰 발급
//            TokenDto tokenDto = securityService.login(resultAccount.getId());
//            loginResponseDto.setAccessToken(tokenDto.getAccessToken());
//            System.out.println("로그인이 확인됐고, 토큰을 발급했습니다.");
//        }
//        return loginResponseDto;
//    }
//    //회원가입 처리 메소드
////    public Long kakaoSignUp(SignupRequestDto requestDto) {
////
////        KakaoAccountDto kakaoAccountDto = getKakaoInfo(requestDto.getKakaoAccessToken());
////        Account account = mapKakaoInfo(kakaoAccountDto);
////
////        // 닉네임, 프로필사진 set
////        String accountName = requestDto.getAccountName();
////        String accountPicture = requestDto.getPicture();
////        account.setAccountName(accountName);
////        account.setPicture(accountPicture);
////
////        // DB에 save
////        accountRepository.save(account);
////
////        // 회원가입 결과로 회원가입한 accountId 리턴
////        return account.getId();
////    }
//}
