package com.loveyourdog.brokingservice.service;

import com.loveyourdog.brokingservice.model.dto.querydsl.AuthorityDto;
import com.loveyourdog.brokingservice.model.entity.Authority;
import com.loveyourdog.brokingservice.model.entity.Customer;
import com.loveyourdog.brokingservice.model.entity.Dogwalker;
import com.loveyourdog.brokingservice.model.enums.AccountType;
import com.loveyourdog.brokingservice.repository.customer.CustomerRespository;
import com.loveyourdog.brokingservice.repository.dogwalker.DogwalkerRepository;
import com.loveyourdog.brokingservice.security.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    @Value("${jwt.live.atk}") // access token 유효시간 : 30분
    private int atkLive;
    @Value("${jwt.live.rtk}") // refresh token 유효시간 : 2주
    private int rtkLive;

    private final DogwalkerRepository dogwalkerRepository;
    private final CustomerRespository customerRespository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RedisService redisService;






    public SignResponse login(SignRequest request) throws Exception {
        Subject atkSubject = null;
        Subject rtkSubject = null;

        if(request.getUserType().equalsIgnoreCase("customer")){
            // 회원 정보 조회(by email)
            Customer customer = customerRespository.findWithRolesByEmail(request.getEmail()).orElseThrow(() ->
                    new BadCredentialsException("잘못된 계정정보입니다.")); // 계정으로 멤버를 찾을 수 없으면
            // password 일치 확인
            if (!passwordEncoder.matches(request.getPwd(), customer.getPwd())) { // 비밀번호가 다르면
                throw new BadCredentialsException("잘못된 계정정보입니다.");
            }

            // 위의 단계를 통과했다면...
            // 회원 정보를 담은 Subject로 atk,rtk 생성
            atkSubject = Subject.atk(customer.getEmail(),"customer");
            rtkSubject = Subject.rtk(customer.getEmail(),"customer");
            String atk = jwtProvider.createToken(atkSubject, atkLive); // 액세스 토큰 생성
            String rtk = jwtProvider.createToken(rtkSubject, rtkLive); // 리프레시 토큰 생성
            redisService.setValues(request.getEmail(), rtk, Duration.ofMillis(rtkLive)); // email, rtk를 redis에 저장

            List<AuthorityDto> authDtos = new ArrayList<>();
            for (Authority auth: customer.getRoles()) {
                AuthorityDto authorityDto = new AuthorityDto();
                authorityDto.setId(auth.getId());
                authorityDto.setName(auth.getName());

                if(auth.getCustomer().getId()!=null){
                    authorityDto.setCustomerId(auth.getCustomer().getId());
                } else {
                    throw  new Exception("customerid 없음");
                }
                authDtos.add(authorityDto);
            }

            //DB에서 찾은 정보,토큰을 Dto에 담아서 반환
            return SignResponse.builder()
                    .customerId(customer.getId())
                    .name(customer.getName())
                    .email(customer.getEmail())
                    .roles(authDtos)
                    .accessToken(atk) // 액세스 토큰
                    .refreshToken(rtk) // 리프레시 토큰
                    .username(customer.getName())
                    .usernick(customer.getNick())
                    .goalCnt(customer.getGoalCnt())
                    .dirName(customer.getDirName())
                    .fileName(customer.getFileName())
                    .extension(customer.getExtension())
                    .build();

        } else if (request.getUserType().equalsIgnoreCase("dogwalker")) {
            // 회원 정보 조회(by email)
            Dogwalker dogwalker = dogwalkerRepository.findWithRolesByEmail(request.getEmail()).orElseThrow(() ->
                    new BadCredentialsException("잘못된 계정정보입니다.")); // 계정으로 멤버를 찾을 수 없으면
            // password 일치 확인
            if (!passwordEncoder.matches(request.getPwd(), dogwalker.getPwd())) { // 비밀번호가 다르면
                throw new BadCredentialsException("잘못된 계정정보입니다.");
            }
            // 위의 단계를 통과했다면...
            // 회원 정보를 담은 Subject로 atk,rtk 생성
            atkSubject = Subject.atk(dogwalker.getEmail(),"dogwalker");
            rtkSubject = Subject.rtk(dogwalker.getEmail(),"dogwalker");

            String atk = jwtProvider.createToken(atkSubject, atkLive); // 액세스 토큰 생성
            String rtk = jwtProvider.createToken(rtkSubject, rtkLive); // 리프레시 토큰 생성
            redisService.setValues(request.getEmail(), rtk, Duration.ofMillis(rtkLive)); // email, rtk를 redis에 저장


            List<AuthorityDto> authDtos = new ArrayList<>();
            for (Authority auth: dogwalker.getRoles()) {
                AuthorityDto authorityDto = new AuthorityDto();
                authorityDto.setId(auth.getId());
                authorityDto.setName(auth.getName());
                if(auth.getDogwalker().getId()!=null){
                    authorityDto.setDogwalkerId(auth.getDogwalker().getId());
                }else {
                    throw new Exception("도그워커id 없음");
                }
                authDtos.add(authorityDto);
            }

            //DB에서 찾은 정보,토큰을 Dto에 담아서 반환
             return SignResponse.builder()
                     .dogwalkerId(dogwalker.getId())
                     .name(dogwalker.getName())
                     .email(dogwalker.getEmail())
                     .roles(authDtos)
                     .accessToken(atk) // 액세스 토큰
                     .refreshToken(rtk) // 리프레시 토큰
                     .username(dogwalker.getName())
                     .usernick(dogwalker.getNick())
                     .goalCnt(dogwalker.getGoalCnt())
                     .dirName(dogwalker.getDirName())
                     .fileName(dogwalker.getFileName())
                     .extension(dogwalker.getExtension())
                     .build();
        } else { // 유저타입 state 미정인 경우
            throw  new BadCredentialsException("usertype이 없습니다");
        }
    }

    public boolean register(SignRequest request) throws Exception {
        //System.out.println("register service 실행");
        try {
            System.out.println("request에 담긴 정보들을 member에 담기");
            System.out.println(request.getUserType());
            System.out.println(request.getEmail());
            System.out.println(request.getPwd());
            System.out.println(request.getAccountType());

            if (request.getUserType().equalsIgnoreCase("customer")) {
                AccountType accountType = null;
                if (request.getAccountType().equalsIgnoreCase("email")) {
                    accountType = AccountType.EMAIL;

                }

                Customer customer = Customer.builder()
                        .email(request.getEmail())
                        .pwd(passwordEncoder.encode(request.getPwd()))
                        .accountType(accountType)
                        .build();
                System.out.println("권한 USER도 member에 담기");
                customer.setRoles(Collections.singletonList(Authority.builder().name("ROLE_USER").build()));
                System.out.println("member를 DB에 저장");
                customerRespository.save(customer);

            } else if (request.getUserType().equalsIgnoreCase("dogwalker")) {
                AccountType accountType = null;
                if (request.getAccountType().equalsIgnoreCase("email")) {
                    accountType = AccountType.EMAIL;
                }
                Dogwalker dogwalker = Dogwalker.builder()
                        .email(request.getEmail())
                        .pwd(passwordEncoder.encode(request.getPwd()))
                        .accountType(accountType)
                        .build();
                System.out.println("권한 USER도 member에 담기");
                dogwalker.setRoles(Collections.singletonList(Authority.builder().name("ROLE_USER").build()));
                System.out.println("member를 DB에 저장");
                dogwalkerRepository.save(dogwalker);
            } else {
                System.out.println("유저 타입 아무것도 아님, 에러처리 필요");
            }


        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new Exception("잘못된 요청입니다.");
        }
        return true;
    }




    public AccountResponse logout(HttpServletRequest request) throws Exception {
        System.out.println("<<    logout!!    >>>");
        String atk = jwtProvider.resolveToken(request);
        atk = atk.split(" ")[1].trim();

        // 1. 레디스에서 refreshToken 삭제
        System.out.println("레디스에서 refreshToken 삭제");
        Subject subject = jwtProvider.getSubject(atk);
        String email = subject.getEmail();
        redisService.delete(email);

        // 2. 레디스에 accessToken 사용못하도록 등록
        System.out.println("레디스에 accessToken 사용못하도록 등록");
        redisService.setBlackList(atk, "accessToken", 5); // 5분동안 로그아웃한 atk를 저장
        //System.out.println("blacklist 등록되었는가? : "+redisService.hasKeyBlackList(accessToken));

        return AccountResponse.builder()
                .email(subject.getEmail())
                .build();
    }

    // 임시 비밀번호 저장
    public void SetTempPassword(String email, String tempPwd, String userType){
        System.out.println("userType"+userType);
        if(userType.equalsIgnoreCase("customer")){
            Customer customer = customerRespository.findWithRolesByEmail(email).orElseThrow(() ->
                    new BadCredentialsException("잘못된 계정정보입니다."));
            customer.setPwd(tempPwd);
            customerRespository.save(customer);
        } else if (userType.equalsIgnoreCase("dogwalker")) {
            Dogwalker dogwalker = dogwalkerRepository.findWithRolesByEmail(email).orElseThrow(() ->
                    new BadCredentialsException("잘못된 계정정보입니다."));
            dogwalker.setPwd(tempPwd);
            dogwalkerRepository.save(dogwalker);
        }

    }


    public boolean checkDuplEmail(UserTypeDto userTypeDto) {
        String email = userTypeDto.getEmail();
        String userType = userTypeDto.getUserType();
        if (userType.equalsIgnoreCase("customer")) {
            return customerRespository.findWithRolesByEmail(email).isEmpty(); // 중복되면 false
        } else if (userType.equalsIgnoreCase("dogwalker")) {
            return dogwalkerRepository.findWithRolesByEmail(email).isEmpty(); // 중복되면 false
        } else {
            return true;
        }
    }

    public boolean checkPwd(UserTypeDto userTypeDto) {
        String pwd = userTypeDto.getPwd();
        String userType = userTypeDto.getUserType();
        Long userId = userTypeDto.getUserId();

        if (userType.equalsIgnoreCase("customer")) {
            Customer customer = customerRespository.findById(userId).orElseThrow(()->{
                throw new NoSuchElementException("해당하는 고객이 없습니다");
            });
            // password 일치 확인
            if (passwordEncoder.matches(pwd, customer.getPwd())) {
                return true;
            }
        } else if (userType.equalsIgnoreCase("dogwalker")) {
            Dogwalker dogwalker = dogwalkerRepository.findById(userId).orElseThrow(()->{
                throw new NoSuchElementException("해당하는 dw 없습니다");
            });
            // password 일치 확인
            if (passwordEncoder.matches(pwd, dogwalker.getPwd())) {
                return true;
            }
        }
        return false;
    }


    public boolean changePwd(UserTypeDto userTypeDto){
        String pwd = userTypeDto.getPwd();
        String userType = userTypeDto.getUserType();
        Long userId = userTypeDto.getUserId();

        if (userType.equalsIgnoreCase("customer")) {
            Customer customer = customerRespository.findById(userId).orElseThrow(()->{
                throw new NoSuchElementException("해당하는 고객이 없습니다");
            });
            customer.setPwd(passwordEncoder.encode(pwd));
            customerRespository.save(customer);
        } else if (userType.equalsIgnoreCase("dogwalker")) {
            Dogwalker dogwalker = dogwalkerRepository.findById(userId).orElseThrow(()->{
                throw new NoSuchElementException("해당하는 dw 없습니다");
            });
            dogwalker.setPwd(passwordEncoder.encode(pwd));
            dogwalkerRepository.save(dogwalker);
        }
        return true;
    }

    public boolean changePhone(UserTypeDto userTypeDto){
        String phone = userTypeDto.getPhone();
        String userType = userTypeDto.getUserType();
        Long userId = userTypeDto.getUserId();

        if (userType.equalsIgnoreCase("customer")) {
            Customer customer = customerRespository.findById(userId).orElseThrow(()->{
                throw new NoSuchElementException("해당하는 고객이 없습니다");
            });
            customer.setPhone(phone);
            customerRespository.save(customer);
        } else if (userType.equalsIgnoreCase("dogwalker")) {
            Dogwalker dogwalker = dogwalkerRepository.findById(userId).orElseThrow(()->{
                throw new NoSuchElementException("해당하는 dw 없습니다");
            });
            dogwalker.setPhone(phone);
            dogwalkerRepository.save(dogwalker);
        }
        return true;
    }
}
