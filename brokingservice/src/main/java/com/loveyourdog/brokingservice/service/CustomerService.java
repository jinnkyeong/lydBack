package com.loveyourdog.brokingservice.service;

import com.loveyourdog.brokingservice.model.dto.querydsl.CommisionDto;
import com.loveyourdog.brokingservice.model.dto.requestDto.CommisionRequestDto;
import com.loveyourdog.brokingservice.model.dto.responseDto.CommisionResponseDto;
import com.loveyourdog.brokingservice.model.dto.responseDto.ProfileResponseDto;
import com.loveyourdog.brokingservice.model.entity.*;
import com.loveyourdog.brokingservice.repository.alarm.AlarmRepository;
import com.loveyourdog.brokingservice.repository.certificate.CertificateRespository;
import com.loveyourdog.brokingservice.repository.commision.CommisionOrderCondition;
import com.loveyourdog.brokingservice.repository.commision.CommisionRepositoryImpl;
import com.loveyourdog.brokingservice.repository.commision.CommisionSearchCondition;
import com.loveyourdog.brokingservice.repository.cusRequire.CusRequireRepository;
import com.loveyourdog.brokingservice.repository.customer.CustomerRespository;
import com.loveyourdog.brokingservice.repository.commision.CommisionRepository;
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
import java.time.format.TextStyle;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomerService {

    private final CommisionRepository commisionRepository;
    private final CommisionRepositoryImpl commisionRepositoryImpl;
    private final CustomerRespository customerRespository;
    private final CusRequireRepository cusRequireRepository;
    private final CertificateRespository certificateRespository;
    private final AlarmRepository alarmRepository;



    private String toWeekday(int month, int day){
        int thisYear = LocalDate.now().getYear(); // 올해
        LocalDate date = LocalDate.of(thisYear, month, day); // 해당일자
        String dayOfWeek = date.getDayOfWeek()
                .getDisplayName(TextStyle.SHORT, Locale.US)
                .toLowerCase(); // 요일(mon,tue...)

        return dayOfWeek;
    }




    // customer id -> profile 정보
    public ProfileResponseDto getCustomer(Long cusId){
        Customer customer = customerRespository.findById(cusId).orElseThrow(
                ()-> new NoSuchElementException("해당 id를 가진 고객은 없습니다"));

        // 최근 한달 내 산책횟수 구하기
        List<Reservation> reservations = new ArrayList<>();
        if(customer.getInquiries().size() > 0){
            for (Inquiry inquiry: customer.getInquiries()) {
                if(inquiry.getReservation()!=null){
                    if(inquiry.getReservation().getEndDt()!=null){
                        if(inquiry.getReservation().getEndDt().plusMonths(1).isAfter(LocalDateTime.now())){ // 1월 이내에 산책종료한 건
                            reservations.add(inquiry.getReservation());
                        }
                    }

                }
            }
        }
        if(customer.getOffers().size() > 0){
            for (Offer offer: customer.getOffers()) {
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
                .userId(cusId)
                .userType("customer")
                .nick(customer.getNick())
                .phone(customer.getPhone())
                .email(customer.getEmail())
                .pwd(customer.getPwd())
                .dirName(customer.getDirName())
                .fileName(customer.getFileName())
                .extension(customer.getExtension())
                .temperture(customer.getTemperture())
                .profileMessage(customer.getProfileMessage())
                .grade(customer.getGrade())
                .goalCnt(customer.getGoalCnt())
                .monthGoalCnt(reservations.size())
                .build();
        return responseDto;
    }

    // 의뢰서 DB에 저장(Customer 테이블, Commision 테이블)
    // parameter : ApplyRequestDto(지원서 목록)
    public boolean createCommision(CommisionRequestDto requestDto) throws AccountNotFoundException {

        // customer 저장
        Customer customer = customerRespository.findById(requestDto.getCustomerId()).orElseThrow(()->
                new AccountNotFoundException("일치하는 고객이 없습니다"));
        customer.setName(requestDto.getName());
        customer.setBirthYear(requestDto.getBirthYear());
        customer.setGen(requestDto.getGen());
        customer.setPhone(requestDto.getPhone());
        customer.setNick(requestDto.getNick());
        customer.setAddrTown(requestDto.getAddrTown());
        customer.setAddrState(requestDto.getAddrState());
        customer.setAddrDetail(requestDto.getAddrDetail());
        customer.setWishCertificates( requestDto.getCertificateKeywords());
        customerRespository.save(customer);

//        List<Certificate> certificates = new ArrayList<>();
////        for (String key: requestDto.getCertificateKeywords()) {
////            Certificate certificate = Certificate.builder()
////                    .keyword(key)
////                    .customer(customer)
////                    .build();
////            certificates.add(certificate);
////        }
////        certificateRespository.saveAll(certificates);

        // commision 저장

        List<String> tempList = new ArrayList<>();
        for (String cr:requestDto.getCusRequires()) {
            if(cr!=null && cr.length()>0){
                tempList.add(cr);
            }
        }
        HashSet<String> tempSet = new HashSet<>(tempList);
        List<String> cusRequires = new ArrayList<>(tempSet);

        Commision commision = Commision.builder()
                .customer(customer)
                .price(requestDto.getPrice())
                .month(requestDto.getMonth())
                .day(requestDto.getDay())
                .weekday(toWeekday(requestDto.getMonth(), requestDto.getDay()))
                .startHour(requestDto.getStartHour())
                .startMin(requestDto.getStartMin())
                .endHour(requestDto.getEndHour())
                .endMin(requestDto.getEndMin())
                .dogName(requestDto.getDogName())
                .dogAge(requestDto.getDogAge())
                .dogWeight(requestDto.getDogWeight())
                .breed(requestDto.getBreed())
                .dogType(requestDto.getDogType())
                .dogAggr(requestDto.getDogAggr())
                .dogHealth(requestDto.getDogHealth())
                .road(requestDto.getRoad())
                .cusRequireStrings(cusRequires)
                .build();
        commisionRepository.save(commision);


//        List<CusRequire> cusRequires = new ArrayList<>();
//        for (String str : requestDto.getCusRequires()) {
//            CusRequire cusRequire = CusRequire.builder()
//                    .context(str)
//                    .commision(commision)
//                    .build();
//            cusRequires.add(cusRequire);
//        }



        return true;
    }


    // () -> commision list
    public List<CommisionResponseDto> getEveryCommisions(){
        List<Commision> commisions = commisionRepository.findAll(
                Sort.by(Sort.Direction.DESC, "id")); // 최근에 추가한 것이 먼저
        List<CommisionResponseDto> responseDtos = new ArrayList<>();
        for (Commision c:commisions) {
            responseDtos.add(new CommisionResponseDto(c));
        }
        return responseDtos;
    }

    // customer id -> commision list
    public List<CommisionResponseDto> getCommisionsByCusId(Long cusId){
        Customer customer  = customerRespository.findById(cusId).orElseThrow(
                ()-> new NoSuchElementException("해당 id를 가진 고객은 없습니다")
        );

        List<CommisionResponseDto> responseDtos = new ArrayList<>();
        for (Commision c : customer.getCommisions()) {
            responseDtos.add(new CommisionResponseDto(c));
        }
        return responseDtos;
    }

    // id -> commision
    public CommisionResponseDto getCommision(Long id){
        Commision commision = commisionRepository.findById(id).orElseThrow(
                ()-> new NoSuchElementException("해당 id를 가진 의뢰서는 없습니다")
        );
        CommisionResponseDto dto = new CommisionResponseDto(commision);
        return dto;
    }




    public boolean isMofiable(Commision commision){
        for (Offer offer:commision.getOffers()) {
            if(offer.getStatus()==2){ // 예약 완료상태 -> 수정불가
                return false;
            }
        }
        for (Inquiry inquiry:commision.getInquiries()){
            if(inquiry.getStatus()==2){
                return false;
            }
        }
        return true;
    }

    public List<Alarm> getCandidateAlarms(Commision commision){
        // customer가 청약자이고 상대방 수락 전에 의뢰서를 변경하려는 경우
        // 상대방 보호를 위하여 상대방에게 알림 필요
        List<Alarm> alarms = new ArrayList<>();
        System.out.println();
        for (Inquiry inquiry:commision.getInquiries()) {
            System.out.println("inquiry for문에 들어옴");
            if(inquiry.getStatus()==1 || inquiry.getStatus()==4){
                Alarm alarm = Alarm.builder()
                        .msgType(1) // 의뢰서 변경
                        .checked(1)
                        .dogwalker(inquiry.getDogwalker()) // 알람 대상 : 의뢰받은 도그워커
                        .paperId(commision.getId())
                        .build();
                alarms.add(alarm);
            }
        }

        return alarms;
    }

    public boolean modifyCommision(Long comId, CommisionRequestDto requestDto) throws Exception {

        if(requestDto.isDtoEntireVariableNull()==true){
            new NullPointerException("requestDto의 모든 필드가 null입니다");
        }
        Commision commision = commisionRepository.findById(comId).orElseThrow(()->
                new AccountNotFoundException("일치하는 지원서가 없습니다"));

        // 예약 후는 수정 불가
        if(!isMofiable(commision)){
            throw new Exception("예약이 완료되어 수정할 수 없습니다");
        }

        // nick
        if(requestDto.getNick()!=null && StringUtils.hasText(requestDto.getNick())){
            commision.getCustomer().setName(requestDto.getName());
        }  else {
            new Exception("nick =  null입니다");
        }
        // gen
        if(requestDto.getGen()!=null && StringUtils.hasText(requestDto.getGen())){
            commision.getCustomer().setGen(requestDto.getGen());
        }  else {
            new Exception("gen이 null입니다");
        }
        // addrstate
        if(requestDto.getAddrState()!=null && StringUtils.hasText(requestDto.getAddrState())){
            commision.getCustomer().setAddrState(requestDto.getAddrState());
        }  else {
            new Exception("addrState이 null입니다");
        }
        // addrtown
        if(requestDto.getAddrTown()!=null && StringUtils.hasText(requestDto.getAddrTown())){
            commision.getCustomer().setAddrTown(requestDto.getAddrTown());
        }  else {
            new Exception("addrTown이 null입니다");
        }
        // birthYear
        int birthYear = 0;
        if(requestDto.getAge() > 0){
            birthYear = LocalDate.now().getYear() - requestDto.getAge(); // 생년 = 올해년도 - 나이
        } else {
            new Exception("나이가 0 이하로 입력되었습니다");
        }
        commision.getCustomer().setBirthYear(birthYear);
        // dogType
        if(requestDto.getDogType() > 0 && StringUtils.hasText(String.valueOf(requestDto.getDogType()))){
            commision.setDogType(requestDto.getDogType());
        }  else {
            new Exception("dogtype null입니다");
        }
        // aggr
        if(requestDto.getDogAggr() > 0 && StringUtils.hasText(String.valueOf(requestDto.getDogAggr()))){
            commision.setDogAggr(requestDto.getDogAggr());
        }  else {
            new Exception("aggr null입니다");
        }
        // health
        if(requestDto.getDogHealth() > 0 && StringUtils.hasText(String.valueOf(requestDto.getDogHealth()))){
            commision.setDogHealth(requestDto.getDogHealth());
        }  else {
            new Exception("health null입니다");
        }
        // price
        if(requestDto.getPrice() > 0 && StringUtils.hasText(String.valueOf(requestDto.getPrice()))) {
            commision.setPrice(requestDto.getPrice());
        }
        // cusrequires
        if(requestDto.getCusRequires()!=null && requestDto.getCusRequires().size() > 0) {
            List<String> tempList = new ArrayList<>();
            for (String cr:requestDto.getCusRequires()) {
                if(cr!=null && cr.length()>0){
                    tempList.add(cr);
                }
            }
            HashSet<String> tempSet = new HashSet<>(tempList);
            List<String> cusRequires = new ArrayList<>(tempSet);
            commision.setCusRequireStrings(cusRequires);
//            List<CusRequire> cusRequires = new ArrayList<>();
//            for (String c : requestDto.getCusRequires()) {
//                // 중복된 데이터 배제
//                for (CusRequire cus : commision.getCusRequireRS()) {
//                    if(cus.getContext().equals(c)){
//                        throw new Exception("Certificate 중복입니다");
//                    }
//                }
//                CusRequire cusRequire = CusRequire.builder()
//                        .context(c)
//                        .commision(commision)
//                        .build();
//                cusRequires.add(cusRequire);
//            }
//            cusRequireRepository.saveAll(cusRequires);
        }else {
            new Exception("cusRequires가 0개입니다");
        }

        commisionRepository.save(commision);





//
//        // certificates
//        if(requestDto.getCertificateKeywords().size() > 0) {
//            List<Certificate> certificates = new ArrayList<>();
//            for (String c : requestDto.getCertificateKeywords()) {
//                // 중복된 데이터 배제
//                for (Certificate cert : commision.getCustomer().getCertificates()) {
//                    if(cert.getKeyword().equals(c)){
//                        throw new Exception("Certificate 중복입니다");
//                    }
//                }
//                Certificate certificate = Certificate.builder()
//                        .keyword(c)
//                        .customer(commision.getCustomer())
//                        .build();
//                certificates.add(certificate);
//            }
//            certificateRespository.saveAll(certificates);
//        }else {
//            new Exception("certificates가 0개입니다");
//        }


        // 수락을 기다리는 offer(=도그워커가 청약자)가 있는 경우 : 알림 저장
        alarmRepository.saveAll(getCandidateAlarms(commision));


        return true;
    }

    // id -> commision 삭제
    public boolean deleteCommision(Long comId){
        commisionRepository.deleteById(comId);
        return true;
    }

    // conditions -> commision list
    public Page<CommisionDto> filterWithConditions(CommisionSearchCondition condition){
        System.out.println("[ service ]");
        CommisionOrderCondition orderCondition = new CommisionOrderCondition();
        List<Sort.Order> orders = new ArrayList<>();
        if (condition.getOrderStr()!=null && condition.getOrderStr().size()>0) {
            for (String str:condition.getOrderStr()) {
                String[] value = str.split("/");
                orderCondition.setDirection(value[0]); // null 경우 DESC 반환
                orderCondition.setProperties(value[1]); // null 경우 view 반환
                orders.add(new Sort.Order(Sort.Direction.fromString(orderCondition.getDirection()),orderCondition.getProperties()));
            }
        }
        return commisionRepositoryImpl.search(
                condition,
                PageRequest.of( // PageRequest 세팅
                        condition.getPage() -1,
                        condition.getSize(),
                        Sort.by(orders)
                )).map(Commision::toCommisionDto);
    }

    public boolean addComViewCnt(Long comId){
        Commision commision = commisionRepository.findById(comId).orElseThrow(()->{
            throw new NoSuchElementException("해당하는 의뢰서가 없습니다");
        });
        commision.setView(commision.getView() + 1);
        commisionRepository.save(commision);
        return true;
    }


}
