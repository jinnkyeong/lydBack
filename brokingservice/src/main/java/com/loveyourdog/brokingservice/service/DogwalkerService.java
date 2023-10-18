package com.loveyourdog.brokingservice.service;

import com.loveyourdog.brokingservice.model.dto.querydsl.ApplicationDto;
import com.loveyourdog.brokingservice.model.dto.querydsl.LocationDto;
import com.loveyourdog.brokingservice.model.dto.requestDto.ApplyModRequestDto;
import com.loveyourdog.brokingservice.model.dto.requestDto.ApplyRequestDto;
import com.loveyourdog.brokingservice.model.dto.responseDto.ApplyDetailResponseDto;
import com.loveyourdog.brokingservice.model.dto.responseDto.ApplyResponseDto;
import com.loveyourdog.brokingservice.model.dto.responseDto.ProfileResponseDto;
import com.loveyourdog.brokingservice.model.dto.responseDto.TestResponseDto;
import com.loveyourdog.brokingservice.model.entity.*;
import com.loveyourdog.brokingservice.repository.alarm.AlarmRepository;
import com.loveyourdog.brokingservice.repository.application.ApplicationRepository;
import com.loveyourdog.brokingservice.repository.application.ApplicationRepositoryImpl;
import com.loveyourdog.brokingservice.repository.application.ApplicationOrderCondition;
import com.loveyourdog.brokingservice.repository.certificate.CertificateRespository;
import com.loveyourdog.brokingservice.repository.customer.CustomerRespository;
import com.loveyourdog.brokingservice.repository.dogwalker.DogwalkerRepository;
import com.loveyourdog.brokingservice.repository.application.ApplicationSearchCondition;
import com.loveyourdog.brokingservice.repository.location.LocationRepository;
import com.loveyourdog.brokingservice.repository.weekday.WeekdayRepository;
import com.loveyourdog.brokingservice.scheduler.Scheduler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import javax.security.auth.login.AccountNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class DogwalkerService {

    private final DogwalkerRepository dogwalkerRepository;
    private final ApplicationRepository applicationRepository;
    private final CertificateRespository certificateRespository;
    private final LocationRepository locationRepository;
    private final WeekdayRepository weekdayRepository;
    private final ApplicationRepositoryImpl applicationRepositoryImpl;
    private final AlarmRepository alarmRepository;
    private final CustomerRespository customerRespository;
    private final Scheduler scheduler;





    // dogwalker id -> profile 정보
    public ProfileResponseDto getDogwalker(Long dwId){

        Dogwalker dogwalker = dogwalkerRepository.findById(dwId).orElseThrow(
                ()-> new NoSuchElementException("해당 id를 가진 dw은 없습니다"));

        // 최근 한달 내 산책횟수 구하기
        List<Reservation> reservations = new ArrayList<>();
        if(dogwalker.getInquiries().size() > 0){
            for (Inquiry inquiry: dogwalker.getInquiries()) {
                if(inquiry.getReservation()!=null){
                    if(inquiry.getReservation().getEndDt()!=null){
                        if(inquiry.getReservation().getEndDt().plusMonths(1).isAfter(LocalDateTime.now())){ // 1월 이내에 산책종료한 건
                            reservations.add(inquiry.getReservation());
                        }


                    }

                }
            }
        }
        if(dogwalker.getOffers().size() > 0){
            for (Offer offer: dogwalker.getOffers()) {
                if(offer.getReservation()!=null){
                    if(offer.getReservation().getEndDt()!=null){
                        if(offer.getReservation().getEndDt().plusMonths(1).isAfter(LocalDateTime.now())){ // 1월 이내에 산책종료한 건
                            reservations.add(offer.getReservation());
                        }
                    }
                }
            }
        }

        ProfileResponseDto responseDto = ProfileResponseDto.builder()
                .userId(dwId)
                .userType("dogwalker")
                .nick(dogwalker.getNick())
                .phone(dogwalker.getPhone())
                .email(dogwalker.getEmail())
                .pwd(dogwalker.getPwd())
                .dirName(dogwalker.getDirName())
                .fileName(dogwalker.getFileName())
                .extension(dogwalker.getExtension())
                .testScore(dogwalker.getTestScore())
                .star(dogwalker.getStar())
                .profileMessage(dogwalker.getProfileMessage())
                .grade(dogwalker.getGrade())
                .goalCnt(dogwalker.getGoalCnt())
                .monthGoalCnt(reservations.size())
                .build();
        return responseDto;
    }

    // dogwalker id -> dogwalker 중 test 정보
    public TestResponseDto getTestByDwId(Long dwId){
        Dogwalker dogwalker = dogwalkerRepository.findById(dwId).orElseThrow(
                ()-> new NoSuchElementException("해당 id를 가진 dw은 없습니다"));

        TestResponseDto responseDto = new TestResponseDto();
        if(dogwalker.getTestScore()!=0){
            responseDto.setTestScore(dogwalker.getTestScore());
        }
        if(dogwalker.getTestStartAt()!=null){
            responseDto.setTestStartAt(dogwalker.getTestStartAt());
        }
        if(dogwalker.getTestEndAt()!=null){
            responseDto.setTestEndAt(dogwalker.getTestEndAt());
        }
        return responseDto;
    }

    //  () -> application list
    public List<ApplyResponseDto> getEveryApplication(){
        List<Application> applications = applicationRepository.findAll();
        List<ApplyResponseDto> responseDtos = new ArrayList<>();

        // Dto list로 변환 후 리턴
        for (Application a :applications) {
            if(a.getDogwalker().isPassed()){ // 합격한 지원자들만
                ApplyResponseDto dto = a.toApplyResponseDto(a);
                responseDtos.add(dto);
            }
        }
        return responseDtos;
    }

    // application id -> application
    public ApplyDetailResponseDto getApplication(Long id){
        Application application = applicationRepository.findById(id).orElseThrow(
                ()-> new NoSuchElementException("해당 id를 가진 지원서는 없습니다")
        );
        ApplyDetailResponseDto dto = application.toApplyDetailResponseDto(application);
        return dto;
    }

    // dogwalker id -> application list
    public List<ApplyResponseDto> getApplicationsByDwId(Long dwId){
        Dogwalker dogwalker = dogwalkerRepository.findById(dwId).orElseThrow(
                ()-> new NoSuchElementException("해당 id를 가진 dw은 없습니다"));
        List<ApplyResponseDto> responseDtos = new ArrayList<>();
        for (Application app : dogwalker.getApplications()) {
            responseDtos.add(app.toApplyResponseDto(app));
        }
        return responseDtos;
    }

    // 연관된 예약이 산책완료 이후인 경우만 수정가능
    public boolean isMofiable(Application application){
        Reservation reservation = null;
        for (Offer offer:application.getOffers()) {
            if(offer.getReservation()!=null && offer.getReservation().getStatus() < 4){
                return false;
            } else {
                return true;
            }
        }
        for (Inquiry inquiry:application.getInquiries()){
            if(inquiry.getReservation()!=null && inquiry.getReservation().getStatus() < 4){
                return false;
            } else {
                return true;
            }
        }
        return true;
    }

    public List<Alarm> getCandidateAlarms(Application application){
        // dogwalker가 청약자이고 상대방 수락 전에 지원서를 변경하려는 경우
        // 상대방 보호를 위하여 상대방에게 알림 필요
        List<Alarm> alarms = new ArrayList<>();
        System.out.println();
        for (Offer offer:application.getOffers()) {
            System.out.println("offer for문에 들어옴");
            if(offer.getStatus()==1 || offer.getStatus()==4){
                Alarm alarm = Alarm.builder()
                        .msgType(1) // 지원서 변경
                        .customer(offer.getCustomer()) // 알람 대상 : 제안받은 고객
                        .paperId(application.getId())
                        .checked(1)
                        .build();
                alarms.add(alarm);
            }
        }

        return alarms;
    }


    // info -> application 수정
    @Transactional
    public boolean modifyApplication(Long appId,ApplyModRequestDto requestDto) throws Exception {
        Application application = applicationRepository.findById(appId).orElseThrow(()->
                new AccountNotFoundException("일치하는 지원서가 없습니다"));

        // 모든 필드가 null인 겨우
        if(requestDto.isDtoEntireVariableNull()==true){
            new NullPointerException("requestDto의 모든 필드가 null입니다");
        }

        // 예약 후는 수정 불가하도록 처리(거래상대방 보호)
        if(!isMofiable(application)){
            throw new Exception("예약이 완료되어 수정할 수 없습니다");
        }

        // name
        if(requestDto.getName()!=null && StringUtils.hasText(requestDto.getName())){
            application.getDogwalker().setName(requestDto.getName());
        }  else {
            new Exception("name이 null입니다");
        }
        // gen
        if(requestDto.getGen()!=null && StringUtils.hasText(requestDto.getGen())){
            application.getDogwalker().setGen(requestDto.getGen());
        }  else {
            new Exception("gen이 null입니다");
        }
        // addrstate
        if(requestDto.getAddrState()!=null && StringUtils.hasText(requestDto.getAddrState())){
            application.getDogwalker().setAddrState(requestDto.getAddrState());
        }  else {
            new Exception("addrState이 null입니다");
        }
        // addrtown
        if(requestDto.getAddrTown()!=null && StringUtils.hasText(requestDto.getAddrTown())){
            application.getDogwalker().setAddrTown(requestDto.getAddrTown());
        }  else {
            new Exception("addrTown이 null입니다");
        }
        // adopted
        if(StringUtils.hasText(String.valueOf(requestDto.isAdopted()))){
            application.setAdopted(requestDto.isAdopted());
        }  else {
            new Exception("isAdopted이 null입니다");
        }
        // adotedmonth
        if(requestDto.getAdoptedMonth()==0 && StringUtils.hasText(String.valueOf(requestDto.getAdoptedMonth()))){
            application.setAdoptedMonth(requestDto.getAdoptedMonth());
        }  else {
            new Exception("AdoptedMonth null입니다");
        }
        // age -> birthYear
        int birthYear = 0;
        if(requestDto.getAge() > 0){
            birthYear = LocalDate.now().getYear() - requestDto.getAge(); // 생년 = 올해년도 - 나이
        } else {
            new Exception("나이가 0 이하로 입력되었습니다");
        }
        application.getDogwalker().setBirthYear(birthYear);
        // adoptedWhich
        if(requestDto.getAdoptedWhich()!=null && StringUtils.hasText(requestDto.getAdoptedWhich())){
            application.setAdoptedWhich(Integer.parseInt(requestDto.getAdoptedWhich())); // int로 변환
        } else {
            new Exception("입양 동물 유형이 잘못 입력 되었습니다");
        }
        // pettype
        if(requestDto.getPetType()!=null && StringUtils.hasText(requestDto.getPetType())){
            application.setPetType(Integer.parseInt(requestDto.getPetType())); // int로 변환
        } else {
            new Exception("원하는 강아지 유형이 잘못 입력 되었습니다");
        }
        // price
        if(requestDto.getPrice() > 0 && StringUtils.hasText(String.valueOf(requestDto.getPrice()))) {
            application.setPrice(requestDto.getPrice());
        }
        // sentence
        if(requestDto.getSentence()!=null && StringUtils.hasText(requestDto.getSentence())) {
            application.setSentence(requestDto.getSentence());
        }
        // locations
        List<Location> locationList =  application.getLocations();
        locationRepository.deleteAll(locationList);
        application.getLocations().clear();
        List<Location> locations = new ArrayList<>();
        List<LocationDto> locationDtos = requestDto.getLocations();
        if(locationDtos.size() > 0) {
            for (LocationDto dto : locationDtos) {
                // dto의 모든 필드가 null인 경우 배제
                if(dto.isDtoEntireVariableNull()==true){
                    new NullPointerException("LocationDto의 모든 필드가 null입니다");
                }
                // 저장
                Location location = Location.builder()
                        .state(dto.getState())
                        .town(dto.getTown())
                        .application(application)
                        .build();
                locations.add(location);
            }
            locationRepository.saveAll(locations);
        } else {
            new Exception("locations가 0개입니다");
        }
//        application.setLocations(locations); "java.utill.ConcurrentModificationException\r\n\tat java.base/java.util.ArrayList.forEach(ArrayList.java:1542)\r\n\tat

        // weekdays
        List<Weekday> weekdayList =  application.getWeekdays();
        weekdayRepository.deleteAll(weekdayList);
        application.getWeekdays().clear();
        if(requestDto.getWeekdayKeywords().size() > 0) {
            List<Weekday> weekdays = new ArrayList<>();
            for (String w : requestDto.getWeekdayKeywords()) {
                Weekday weekday = Weekday.builder()
                        .day(w)
                        .application(application)
                        .build();
                weekdays.add(weekday);
            }
            weekdayRepository.saveAll(weekdays);
        }else {
            new Exception("weekdays가 0개입니다");
        }

        // certificates
        // 전부 삭제 후 새로운 자격증 추가
        List<Certificate> certList =  application.getDogwalker().getCertificates();
        certificateRespository.deleteAll(certList);
        application.getDogwalker().getCertificates().clear();
        if(requestDto.getCertificateKeywords().size() > 0) { // 새로운 자격증 ->  저장
            List<Certificate> certificates = new ArrayList<>();
            for (String c : requestDto.getCertificateKeywords()) {
                Certificate certificate = Certificate.builder()
                        .keyword(c)
                        .dogwalker(application.getDogwalker())
                        .build();
                certificates.add(certificate);
            }
            certificateRespository.saveAll(certificates);
        }else {
            new Exception("certificates가 0개입니다");
        }


        // Application 저장
        applicationRepository.save(application);

        // 알림 저장 - 수락을 기다리는 offer(=도그워커가 청약자)가 있는 경우
        alarmRepository.saveAll(getCandidateAlarms(application));

        return true;
    }

    public boolean deleteApplication(Long appId){
        applicationRepository.deleteById(appId);
        return true;
    }


    // 지원서 DB에 저장(Dogwalker 테이블, Application 테이블)
    // parameter : ApplyRequestDto(지원서 목록)
    public ApplicationDto createApplication(ApplyRequestDto requestDto) throws AccountNotFoundException {

        Dogwalker dogwalker = dogwalkerRepository.findById(requestDto.getDogwalkerId()).orElseThrow(()->
            new AccountNotFoundException("일치하는 도그워커를 찾을 수 없습니다 "));

        // save가 찾아서 업데이트 하길 기대
        dogwalker.setName(requestDto.getName());
        dogwalker.setBirthYear(requestDto.getBirthYear());
        dogwalker.setGen(requestDto.getGen());
        dogwalker.setPhone(requestDto.getPhone());
        dogwalker.setNick(requestDto.getNick());
        dogwalker.setAddrState(requestDto.getAddrState());
        dogwalker.setAddrTown(requestDto.getAddrTown());
        dogwalker.setAddrDetail(requestDto.getAddrDetail());
        dogwalker.setDepositBank(requestDto.getDepositBank());
        dogwalker.setDepositAccount(requestDto.getDepositAccount());

        createCertificate(requestDto.getCertificateKeywords(),requestDto.getDogwalkerId()); // 자격증 save

        dogwalkerRepository.save(dogwalker);

        Application application = Application.builder()
                .dogwalker(dogwalker) // 이게 맞나??
                .adopted(requestDto.isAdopted())
                .adoptedWhich(requestDto.getAdoptedWhich())
                .adoptedMonth(requestDto.getAdoptedMonth())
                .petType(requestDto.getPetType())
                .price(requestDto.getPrice())
                .sentence(requestDto.getSentence()).build();
        applicationRepository.save(application);


        List<Location> locations = new ArrayList<>();
        for (LocationDto dto: requestDto.getLocations()) {
            Location location = Location.builder()
                    .state(dto.getState())
                    .town(dto.getTown())
                    .application(application)
                    .build();
            locations.add(location);
        }
        locationRepository.saveAll(locations);


        List<Weekday> weekdays = new ArrayList<>();
        for (String weekName : requestDto.getWeekdayNames()) {
            String day = null;
            switch (weekName){
                case "일요일" : day = "sun";
                    break;
                case "월요일" : day = "mon";
                    break;
                case "화요일" : day = "tue";
                    break;
                case "수요일" : day = "wed";
                    break;
                case "목요일" : day = "thu";
                    break;
                case "금요일" : day = "fri";
                    break;
                case "토요일" : day = "sat";
                    break;
                default:    day = "";
                    break;
            }
            Weekday weekday = Weekday.builder()
                    .day(day)
                    .application(application)
                    .build();
            weekdays.add(weekday);
        }
        weekdayRepository.saveAll(weekdays);




        ApplicationDto dto =  ApplicationDto.builder()
//                .name("true")
                .build();

        return dto;
    }

    public boolean createCertificate(List<String> keywords, Long dogwalkerId){
        for (int i = 0; i < keywords.size(); i++) {
            Dogwalker dogwalker = dogwalkerRepository.findById(dogwalkerId).orElseThrow(
                    () -> new NoSuchElementException("해당하는 도그워커가 없습니다"));

            Certificate certificate = Certificate.builder()
                    .keyword(keywords.get(i))
                    .dogwalker(dogwalker).build();

            certificateRespository.save(certificate);
        }
        return true;
    }

    public Page<ApplicationDto> filterWithConditions(ApplicationSearchCondition condition){

        ApplicationOrderCondition orderCondition = new ApplicationOrderCondition();
        List<Sort.Order> orders = new ArrayList<>();
        if (condition.getOrderStr()!=null && condition.getOrderStr().size()>0) {
            for (String str:condition.getOrderStr()) {
                String[] value = str.split("/");
                orderCondition.setDirection(value[0]); // null 경우 DESC 반환
                orderCondition.setProperties(value[1]); // null 경우 view 반환
                orders.add(new Sort.Order(Sort.Direction.fromString(orderCondition.getDirection()),orderCondition.getProperties()));
            }
        }

        return applicationRepositoryImpl.search(
                        condition,
                        PageRequest.of( // PageRequest 세팅
                                condition.getPage()-1,
                                condition.getSize(),
                                Sort.by(orders)
                        ))
                .map(Application::toApplicationDto);

    }

    public boolean addAppViewCnt(Long appId){
        Application application = applicationRepository.findById(appId).orElseThrow(()->{
            throw new NoSuchElementException("해당하는 지원서가 없습니다");
        });
        application.setView(application.getView() + 1);
        applicationRepository.save(application);
        return true;
    }

    public List<ApplyResponseDto> getPopularAppsInmyTown(Long cusId){
        List<ApplyResponseDto> responseDtos = new ArrayList<>();
        List<Application> totalApplicaitons = new ArrayList<>();
        List<Application> finalApplicaitons = new ArrayList<>();
        Customer customer = customerRespository.findById(cusId).orElseThrow(()-> {
                    throw new NoSuchElementException("해당하는 고객이 없습니다");
                });
        String cusState = customer.getAddrState();
        String cusTown = customer.getAddrTown();

        List<Dogwalker> dogwalkers = dogwalkerRepository.findByAddrStateAndAddrTown(cusState, cusTown); // 같은동네 모든 도그워커
        for (Dogwalker dogwalker:dogwalkers) {
            totalApplicaitons.addAll(dogwalker.getApplications()); // 그들의 지원서 전부
        }
        finalApplicaitons =  scheduler.rankPolpular(totalApplicaitons);

        // Dto list로 변환 후 리턴
        for (Application a :finalApplicaitons) {
            if(a.getDogwalker().isPassed()){ // 합격한 지원자들만
                ApplyResponseDto dto = a.toApplyResponseDto(a);
                responseDtos.add(dto);
            }
        }
        return responseDtos;
    }

}
