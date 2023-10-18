package com.loveyourdog.brokingservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.loveyourdog.brokingservice.model.auth.KakaoProfile;
import com.loveyourdog.brokingservice.model.auth.KakaoRequestDto;
import com.loveyourdog.brokingservice.model.auth.OAuthToken;
import com.loveyourdog.brokingservice.model.entity.Authority;
import com.loveyourdog.brokingservice.model.entity.Customer;
import com.loveyourdog.brokingservice.model.entity.Dogwalker;
import com.loveyourdog.brokingservice.model.enums.AccountType;
import com.loveyourdog.brokingservice.repository.customer.CustomerRespository;
import com.loveyourdog.brokingservice.repository.dogwalker.DogwalkerRepository;
import com.loveyourdog.brokingservice.security.JwtProvider;
import com.loveyourdog.brokingservice.security.SignResponse;
import com.loveyourdog.brokingservice.security.Subject;
import com.loveyourdog.brokingservice.security.TokenRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.time.Duration;
import java.util.Collections;
import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final DogwalkerRepository dogwalkerRepository;
    private final CustomerRespository customerRespository;
    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;
    @Value("${spring.security.oauth2.client.registration.kakao.authorization-grant-type}")
    private String authorizationGrantType;

    @Value("${jwt.live.atk}") // access token 유효시간 : 30분
    private int atkLive;
    @Value("${jwt.live.rtk}") // refresh token 유효시간 : 2주
    private int rtkLive;

    private final JwtProvider jwtProvider;
    private final RedisService redisService;



    public String getKakaoUrl(){
        return "https://kauth.kakao.com/oauth/authorize?" +
                "client_id=" + clientId +
                "&redirect_uri=" + redirectUri +
                "&response_type=code";
    }
//    https://kauth.kakao.com/oauth/authorize?client_id=939adb4627ac2f9e1653f58dc85e8ff3&redirect_uri=http://localhost:8090/api/auth/kakao&response_type=code

    // code ->
    public OAuthToken getKakaoAccessToken(String code){

        RestTemplate template = new RestTemplate();
        // HttpHeader 객체 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HttpBody 객체 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type",authorizationGrantType);
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);

        // 헤더와 바디를 HttpEntity에 담기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        // access token 요청 하기(post 방식, response 받음)
        ResponseEntity<String> response = template.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class);


        // 받은 응답(json형식 데이터)을 ObjectMaapper에 담음
        ObjectMapper objectMapper = new ObjectMapper();
        OAuthToken oAuthToken = null;
        try {
            oAuthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
//         System.out.println("액세스 토근 : "+oAuthToken.getAccess_token());
        return oAuthToken;
    }

    public KakaoProfile getKakaoProfile(OAuthToken oAuthToken) throws Exception {
        RestTemplate template = new RestTemplate();

        // HttpHeader 객체 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + oAuthToken.getAccess_token());
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // 헤더와 바디를 HttpEntity에 담기
        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headers);

        // 사용자정보 요청 하기(post 방식, response 받음)
        ResponseEntity<String> response = template.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoProfileRequest,
                String.class);
//        System.out.println("response : "+response.getBody()); // 나옴

        KakaoProfile kakaoProfile = null;
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(response.getBody());
        System.out.println("element.getAsJsonObject()"+element.getAsJsonObject());

        String nickname = element.getAsJsonObject().get("properties").getAsJsonObject().get("nickname").getAsString();
        String profile_image = element.getAsJsonObject().get("properties").getAsJsonObject().get("profile_image").getAsString();
        String email = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("email").getAsString();
        Long id = element.getAsJsonObject().get("id").getAsLong();

        kakaoProfile = KakaoProfile.builder()
                .nickname(nickname)
                .profile_image(profile_image)
                .email(email)
                .id(id)
                .build();


        return kakaoProfile;

    }

    public SignResponse kakaoLogin(KakaoRequestDto requestDto) throws Exception {
        Subject atkSubject = null;
        Subject rtkSubject = null;
        SignResponse response = new SignResponse();


        OAuthToken oAuthToken =  getKakaoAccessToken(requestDto.getCode()); // code로 access token 요청해서 받기
        KakaoProfile kakaoProfile = getKakaoProfile(oAuthToken); // access token으로 유저정보 받기

        if(requestDto.getUserType().equalsIgnoreCase("dogwalker")) {
            if (dogwalkerRepository.findWithRolesByEmail(kakaoProfile.getEmail()).isEmpty()) { // email과 일치하는 유저가 없는 경우(첫 로그인) : 회원정보 저장, 로그인 처리
                // 회원정보 저장
                Dogwalker dw = Dogwalker.builder()
                        .accountType(AccountType.KAKAO)
                        .socialId(kakaoProfile.getId())
                        .nick(kakaoProfile.getNickname())
                        .email(kakaoProfile.getEmail())
                        .build();
                dw.setRoles(Collections.singletonList(Authority.builder().name("ROLE_USER").build()));
                dogwalkerRepository.save(dw);
                // response 세팅
                response.setNick(kakaoProfile.getNickname());
                response.setName(kakaoProfile.getNickname());
            } else { // email과 일치하는 유저가 있는 경우
                Dogwalker dogwalker = dogwalkerRepository.findWithRolesByEmail(kakaoProfile.getEmail()).get();
                // 다른 account type인 경우 : 중복 이메일로 막아야 함
                if(!dogwalker.getAccountType().equals(AccountType.KAKAO)){
                    throw new BadCredentialsException("이메일 중복");
                } 
                // 카카오 로그인을 했던 사람인 경우 : 로그인 처리
                response.setNick(dogwalker.getNick());
                response.setUsernick(dogwalker.getNick());
                response.setName(dogwalker.getName());
                response.setDogwalkerId(dogwalker.getId());
                response.setGoalCnt(dogwalker.getGoalCnt());
                response.setDirName(dogwalker.getDirName());
                response.setFileName(dogwalker.getFileName());
                response.setExtension(dogwalker.getExtension());
            }
            // 회원 정보를 담은 Subject로 atk,rtk 생성
            atkSubject = Subject.atk(kakaoProfile.getEmail(), "dogwalker");
            rtkSubject = Subject.rtk(kakaoProfile.getEmail(), "dogwalker");
            String atk = jwtProvider.createToken(atkSubject, atkLive); // 액세스 토큰 생성
            String rtk = jwtProvider.createToken(rtkSubject, rtkLive); // 리프레시 토큰 생성
            redisService.setValues(kakaoProfile.getEmail(), rtk, Duration.ofMillis(rtkLive));
            response.setAccessToken(atk);
            response.setRefreshToken(rtk);
            return response;
        } else if(requestDto.getUserType().equalsIgnoreCase("customer")){
            if (customerRespository.findWithRolesByEmail(kakaoProfile.getEmail()).isEmpty()) {
                // 회원정보 저장

                Customer cus = Customer.builder()
                        .accountType(AccountType.KAKAO)
                        .socialId(kakaoProfile.getId())
                        .nick(kakaoProfile.getNickname())
                        .email(kakaoProfile.getEmail())
                        .build();
                cus.setRoles(Collections.singletonList(Authority.builder().name("ROLE_USER").build()));
                customerRespository.save(cus);
                // response 세팅
                response.setNick(kakaoProfile.getNickname());
                response.setUsernick(kakaoProfile.getNickname());
                response.setName(kakaoProfile.getNickname());
            } else {
                Customer customer = customerRespository.findWithRolesByEmail(kakaoProfile.getEmail()).get();
                // 다른 account type인 경우 : 중복 이메일로 막아야 함
                if(!customer.getAccountType().equals(AccountType.KAKAO)){
                    throw new BadCredentialsException("이메일 중복");
                }
                // 카카오 로그인을 했던 사람인 경우 : 로그인 처리
                response.setNick(customer.getNick());
                response.setUsernick(customer.getNick());
                response.setName(customer.getName());
                response.setDogwalkerId(customer.getId());
                response.setGoalCnt(customer.getGoalCnt());
                response.setDirName(customer.getDirName());
                response.setFileName(customer.getFileName());
                response.setExtension(customer.getExtension());
            }
            // 회원 정보를 담은 Subject로 atk,rtk 생성
            atkSubject = Subject.atk(kakaoProfile.getEmail(), "customer");
            rtkSubject = Subject.rtk(kakaoProfile.getEmail(), "customer");
            String atk = jwtProvider.createToken(atkSubject, atkLive); // 액세스 토큰 생성
            String rtk = jwtProvider.createToken(rtkSubject, rtkLive); // 리프레시 토큰 생성
            redisService.setValues(kakaoProfile.getEmail(), rtk, Duration.ofMillis(rtkLive));
            response.setAccessToken(atk);
            response.setRefreshToken(rtk);
            return response;
        } else { // 유저타입 state 미정인 경우
            throw  new BadCredentialsException("usertype이 없습니다");
        }
    }
    public SignResponse findUserId(TokenRequest tokenRequest) throws JsonProcessingException {
        String atk = tokenRequest.getAccessToken();
        Subject subject = jwtProvider.getSubject(atk);

        SignResponse response = new SignResponse();
        if(tokenRequest.getUserType().equalsIgnoreCase("dogwalker")){
            Dogwalker dogwalker = dogwalkerRepository.findWithRolesByEmail(subject.getEmail()).orElseThrow(()-> new BadCredentialsException("해당하는 유저가 없습니다"));
            response.setDogwalkerId(dogwalker.getId());
            System.out.println("dogwalker.getId() : "+dogwalker.getId());

        } else if(tokenRequest.getUserType().equalsIgnoreCase("customer")){
            Customer customer = customerRespository.findWithRolesByEmail(subject.getEmail()).orElseThrow(()-> new BadCredentialsException("해당하는 유저가 없습니다"));
            response.setCustomerId(customer.getId());
            System.out.println("customer.getId() : "+customer.getId());


        } else {
            throw  new BadCredentialsException("usertype이 없습니다");
        }
        return response;

    }
}
