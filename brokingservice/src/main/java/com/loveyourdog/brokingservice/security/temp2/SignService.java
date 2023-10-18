package com.loveyourdog.brokingservice.security.temp2;//package com.loveyourdog.brokingservice.security.temp2;
//
//import com.loveyourdog.brokingservice.model.entity.Authority;
//import com.loveyourdog.brokingservice.model.entity.Customer;
//import com.loveyourdog.brokingservice.model.entity.Dogwalker;
//import com.loveyourdog.brokingservice.repository.CustomerRespository;
//import com.loveyourdog.brokingservice.repository.DogwalkerRespository;
//import com.loveyourdog.brokingservice.security.*;
//import com.loveyourdog.brokingservice.service.RedisService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.transaction.Transactional;
//import java.time.Duration;
//import java.util.Collections;
//
//@Service
//@Transactional
//@RequiredArgsConstructor
//public class SignService {
//
//
//    @Value("${jwt.live.atk}") // access token 유효시간 : 30분
//    private int atkLive;
//    @Value("${jwt.live.rtk}") // refresh token 유효시간 : 2주
//    private int rtkLive;
//
//    private final DogwalkerRespository dogwalkerRespository;
//    private final CustomerRespository customerRespository;
//    private final PasswordEncoder passwordEncoder;
//    private final JwtProvider jwtProvider;
//    private final RedisService redisService;
//
//    public SignResponse login(SignRequest request) throws Exception {
//        Subject atkSubject = null;
//        Subject rtkSubject = null;
//        SignResponse signResponse = null;
//
//
//        if(request.getUserType().equalsIgnoreCase("customer")){
//            // 회원 정보 조회(by email)
//            Customer customer = customerRespository.findByEmail(request.getEmail()).orElseThrow(() ->
//                    new BadCredentialsException("잘못된 계정정보입니다.")); // 계정으로 멤버를 찾을 수 없으면
//            // password 일치 확인
//            if (!passwordEncoder.matches(request.getPwd(), customer.getPwd())) { // 비밀번호가 다르면
//                throw new BadCredentialsException("잘못된 계정정보입니다.");
//            }
//
//            // 위의 단계를 통과했다면...
//            // 회원 정보를 담은 Subject로 atk,rtk 생성
//            atkSubject = Subject.atk(customer.getEmail(),"customer");
//            rtkSubject = Subject.rtk(customer.getEmail(),"customer");
//            signResponse = new SignResponse(customer);
//
//
//        } else if (request.getUserType().equalsIgnoreCase("dogwalker")) {
//            // 회원 정보 조회(by email)
//            Dogwalker dogwalker = dogwalkerRespository.findByEmail(request.getEmail()).orElseThrow(() ->
//                    new BadCredentialsException("잘못된 계정정보입니다.")); // 계정으로 멤버를 찾을 수 없으면
//            // password 일치 확인
//            if (!passwordEncoder.matches(request.getPwd(), dogwalker.getPwd())) { // 비밀번호가 다르면
//                throw new BadCredentialsException("잘못된 계정정보입니다.");
//            }
//            // 위의 단계를 통과했다면...
//            // 회원 정보를 담은 Subject로 atk,rtk 생성
//            atkSubject = Subject.atk(dogwalker.getEmail(),"dogwalker");
//            rtkSubject = Subject.rtk(dogwalker.getEmail(),"dogwalker");
//            signResponse = new SignResponse(dogwalker);
//
//
//        } else {
//            System.out.println("도그워커나 고객이 아님/// 에러처리 필요");
//        }
//
//
//        String atk = jwtProvider.createToken(atkSubject, atkLive); // 액세스 토큰 생성
//        String rtk = jwtProvider.createToken(rtkSubject, rtkLive); // 리프레시 토큰 생성
//        redisService.setValues(request.getEmail(), rtk, Duration.ofMillis(rtkLive)); // email, rtk를 redis에 저장
//
//
////        return SignResponse.builder()
////                .id(member.getId())
////                .account(member.getAccount())
////                .name(member.getName())
////                .email(member.getEmail())
////                .nickname(member.getNickname())
////                .roles(member.getRoles())
////                .accessToken(atk) // 액세스 토큰
////                .refreshToken(rtk) // 리프레시 토큰
////                .build();
//        // DB에서 찾은 정보,토큰을 Dto에 담아서 반환
//        return signResponse;
//    }
//
//    public boolean register(SignRequest request) throws Exception {
//        //System.out.println("register service 실행");
//        try {
//            System.out.println("request에 담긴 정보들을 member에 담기");
//            if (request.getUserType().equalsIgnoreCase("customer")) {
//                Customer customer = Customer.builder()
//                        .email(request.getEmail())
//                        .pwd(passwordEncoder.encode(request.getPwd()))
//                        .build();
//                System.out.println("권한 USER도 member에 담기");
//                customer.setRoles(Collections.singletonList(Authority.builder().name("ROLE_USER").build()));
//                System.out.println("member를 DB에 저장");
//                customerRespository.save(customer);
//
//            } else if (request.getUserType().equalsIgnoreCase("dogwalker")) {
//                Dogwalker dogwalker = Dogwalker.builder()
//                        .email(request.getEmail())
//                        .pwd(passwordEncoder.encode(request.getPwd()))
//                        .build();
//                System.out.println("권한 USER도 member에 담기");
//                dogwalker.setRoles(Collections.singletonList(Authority.builder().name("ROLE_USER").build()));
//                System.out.println("member를 DB에 저장");
//                dogwalkerRespository.save(dogwalker);
//            } else {
//                System.out.println("유저 타입 아무것도 아님, 에러처리 필요");
//            }
//
//
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//            throw new Exception("잘못된 요청입니다.");
//        }
//        return true;
//    }
//
////    public SignResponse getMember(String account) throws Exception {
////        //System.out.println("id로 유저정보 조회");
////        Member member = memberRepository.findByAccount(account)
////                .orElseThrow(() -> new Exception("계정을 찾을 수 없습니다."));
////        return new SignResponse(member);
////    }
//
//
//
//    public AccountResponse logout(HttpServletRequest request) throws Exception {
//        System.out.println("<<    logout!!    >>>");
//        String atk = jwtProvider.resolveToken(request);
//        atk = atk.split(" ")[1].trim();
//
//        // 1. 레디스에서 refreshToken 삭제
//        System.out.println("레디스에서 refreshToken 삭제");
//        Subject subject = jwtProvider.getSubject(atk);
//        String email = subject.getEmail();
//        redisService.delete(email);
//
//        // 2. 레디스에 accessToken 사용못하도록 등록
//        System.out.println("레디스에 accessToken 사용못하도록 등록");
//        redisService.setBlackList(atk, "accessToken", 5); // 5분동안 로그아웃한 atk를 저장
//        //System.out.println("blacklist 등록되었는가? : "+redisService.hasKeyBlackList(accessToken));
//
//        return AccountResponse.builder()
//                .email(subject.getEmail())
//                .build();
//    }
//
//
//}