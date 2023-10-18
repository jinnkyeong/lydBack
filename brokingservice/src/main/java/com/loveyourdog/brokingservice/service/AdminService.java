package com.loveyourdog.brokingservice.service;

import com.loveyourdog.brokingservice.model.dto.querydsl.AuthorityDto;
import com.loveyourdog.brokingservice.model.dto.querydsl.BasicRequireDto;
import com.loveyourdog.brokingservice.model.dto.querydsl.CusRequireDto;
import com.loveyourdog.brokingservice.model.dto.requestDto.AdminSignRequestDto;
import com.loveyourdog.brokingservice.model.dto.requestDto.ReservationRequestDto;
import com.loveyourdog.brokingservice.model.dto.responseDto.BasicRequireResponseDto;
import com.loveyourdog.brokingservice.model.dto.responseDto.CommisionResponseDto;
import com.loveyourdog.brokingservice.model.dto.responseDto.DogwalkerResponseDto;
import com.loveyourdog.brokingservice.model.dto.responseDto.ReservationResponseDto;
import com.loveyourdog.brokingservice.model.entity.*;
import com.loveyourdog.brokingservice.model.enums.AccountType;
import com.loveyourdog.brokingservice.repository.admin.AdminRepository;
import com.loveyourdog.brokingservice.repository.basicRequire.BasicRequireRepository;
import com.loveyourdog.brokingservice.repository.cusRequire.CusRequireRepository;
import com.loveyourdog.brokingservice.repository.customer.CustomerRespository;
import com.loveyourdog.brokingservice.repository.dogwalker.DogwalkerRepository;
import com.loveyourdog.brokingservice.repository.inquiry.InquiryRepository;
import com.loveyourdog.brokingservice.repository.offer.OfferRepository;
import com.loveyourdog.brokingservice.repository.reservation.ReservationRepository;
import com.loveyourdog.brokingservice.security.JwtProvider;
import com.loveyourdog.brokingservice.security.SignRequest;
import com.loveyourdog.brokingservice.security.SignResponse;
import com.loveyourdog.brokingservice.security.Subject;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;

import javax.security.auth.login.AccountNotFoundException;
import javax.transaction.Transactional;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminService {



    private final DogwalkerRepository dogwalkerRepository;
    private final CustomerRespository customerRespository;
    private final AdminRepository adminRepository;
    private final ReservationRepository reservationRepository;



    @Value("${jwt.live.atk}") // access token 유효시간 : 30분
    private int atkLive;
    @Value("${jwt.live.rtk}") // refresh token 유효시간 : 2주
    private int rtkLive;


    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RedisService redisService;

//
//    public boolean register(AdminSignRequestDto requestDto) throws Exception {
//        try {
//            Admin admin = Admin.builder()
//                    .id(requestDto.getId())
//                    .pwd(requestDto.getPwd())
//                    .build();
//
//            admin.setRoles(Collections.singletonList(Authority.builder().name("ROLE_ADMIN").build()));
//            adminRepository.save(admin);
//
//
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//            throw new Exception("잘못된 요청입니다.");
//        }
//        return true;
//    }





    public SignResponse login(AdminSignRequestDto requestDto) throws Exception {
        Subject atkSubject = null;
        Subject rtkSubject = null;

        System.out.println("requestDto.getSign() : "+requestDto.getSign());
        System.out.println("requestDto.getPwd() : "+requestDto.getPwd());


            // 회원 정보 조회
        Admin admin = adminRepository.findWithRolesBySign(requestDto.getSign()).orElseThrow(() ->
                new BadCredentialsException("해당 adminId로 찾을 수 없습니다. 잘못된 계정정보입니다.")); // 계정으로 멤버를 찾을 수 없으면

            // password 일치 확인
            if (!requestDto.getPwd().equals( admin.getPwd())) { // 비밀번호가 다르면
                throw new BadCredentialsException("비밀번호가 다릅니다.잘못된 계정정보입니다.");
            }

            // 위의 단계를 통과했다면...
            // 회원 정보를 담은 Subject로 atk,rtk 생성
            atkSubject = Subject.atk(admin.getSign(),"admin");
            rtkSubject = Subject.rtk(admin.getSign(),"admin");
            String atk = jwtProvider.createToken(atkSubject, atkLive); // 액세스 토큰 생성
            String rtk = jwtProvider.createToken(rtkSubject, rtkLive); // 리프레시 토큰 생성
            redisService.setValues(admin.getSign(), rtk, Duration.ofMillis(rtkLive)); // email, rtk를 redis에 저장

            List<AuthorityDto> authDtos = new ArrayList<>();
            for (Authority auth: admin.getRoles()) {
                AuthorityDto authorityDto = new AuthorityDto();
                authorityDto.setId(auth.getId());
                authorityDto.setName(auth.getName());

                if(auth.getAdmin().getId()!=null){
                    authorityDto.setAdminId(auth.getAdmin().getId());
                } else {
                    throw  new Exception("admin id 없음");
                }
                authDtos.add(authorityDto);
            }

            //DB에서 찾은 정보,토큰을 Dto에 담아서 반환
            return SignResponse.builder()
                    .adminId(admin.getId())

                    .sign(admin.getSign())
                    .accessToken(atk)
                    .build();


    }




    public List<DogwalkerResponseDto> getDogwalkers(){
        List<DogwalkerResponseDto> responseDtos = new ArrayList<>();
        List<Dogwalker> dogwalkers =  dogwalkerRepository.findAll(
                Sort.by(Sort.Direction.DESC, "id")); // 최근에 추가한 것이 먼저
        for (Dogwalker dogwalker : dogwalkers) {
            responseDtos.add(new DogwalkerResponseDto(dogwalker));
        }
        return responseDtos;
    }
    public boolean passDw(Long dwId, int passed) throws AccountNotFoundException {
        Dogwalker dogwalker = dogwalkerRepository.findById(dwId).orElseThrow(()->
                new AccountNotFoundException("일치하는 dw가 없습니다"));

        if(passed==1){
            dogwalker.setPassed(true);
        }
        dogwalkerRepository.save(dogwalker);

        // 합격 알림
        Alarm alarm = Alarm.builder()
                .msgType(3)
                .checked(1)
                .dogwalker(dogwalker)
                .build();

        return true;
    }
    public boolean passAppl(Long dwId, int passed) throws AccountNotFoundException {
        Dogwalker dogwalker = dogwalkerRepository.findById(dwId).orElseThrow(()->
                new AccountNotFoundException("일치하는 dw가 없습니다"));

        if(passed==1){
            dogwalker.setAppicationPassed(true);
        }
        dogwalkerRepository.save(dogwalker);
        return true;
    }
    public boolean passInterview(Long dwId, int passed) throws AccountNotFoundException {
        Dogwalker dogwalker = dogwalkerRepository.findById(dwId).orElseThrow(()->
                new AccountNotFoundException("일치하는 dw가 없습니다"));

        if(passed==1){
            dogwalker.setInterviewPassed(true);
        }
        dogwalkerRepository.save(dogwalker);
        return true;
    }

    public boolean startCalculate(Long reservId){
        Reservation reservation = reservationRepository.findById(reservId).orElseThrow(()->{
            throw new NoSuchElementException("해당하는 예약이 없습니다");
        });
        reservation.setStatus(5); //상태변경
        return true;
    }
    public boolean endCalculate(Long reservId){
        Reservation reservation = reservationRepository.findById(reservId).orElseThrow(()->{
            throw new NoSuchElementException("해당하는 예약이 없습니다");
        });
        // 정산완료 시점에 리뷰작성이 안되어있으면 : star 7
        Dogwalker dogwalker = null;
        if(reservation.getReview()==null){
            if(reservation.getInquiry()!=null){
                dogwalker =  reservation.getInquiry().getDogwalker();
            } else if(reservation.getOffer()!=null){
                dogwalker =  reservation.getOffer().getDogwalker();
            }

            int newStar = getAverageStar(dogwalker.getStar(), // 기존 star
                    dogwalker.getGoalCnt()-1, // 기존 goalcnt
                    7);
            dogwalker.setStar(newStar);
            String gradeDw = getDogwalkerGrade(dogwalker.getGoalCnt(), newStar);
            dogwalker.setGrade(gradeDw);
            dogwalkerRepository.save(dogwalker);
        }
        reservation.setStatus(6); //상태변경
        return true;
    }

    // 별점 다시 평균내기
    //preGoalCnt : 해당 예약에서 추가되기 전
    public int getAverageStar(int preStar, int preGoalCnt, int addStar){
        if(preGoalCnt<=0){
            return (preStar * preGoalCnt + addStar) / (preGoalCnt + 2);
        } else {
            return (preStar * preGoalCnt + addStar) / (preGoalCnt + 1);

        }
    }

    public String getDogwalkerGrade(int goalCnt, int star){
        int score = 0;
        score = (int) (goalCnt*0.1 + star*0.9);
        if(score > 20){
            return "A";
        } else if(score > 10){
            return "B";
        } else{
            return "C";
        }
    }
    public String getCustomerGrade(int goalCnt, int temperture){
        int score = 0;
        score = (int) (goalCnt*0.1 + temperture*0.9);
        if(score > 20){
            return "A";
        } else if(score > 10){
            return "B";
        } else{
            return "C";
        }
    }

}
