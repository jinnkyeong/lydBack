package com.loveyourdog.brokingservice.service;

import com.loveyourdog.brokingservice.model.dto.obj.Image;
import com.loveyourdog.brokingservice.model.dto.querydsl.BasicRequireDto;
import com.loveyourdog.brokingservice.model.dto.querydsl.CusRequireDto;
import com.loveyourdog.brokingservice.model.dto.requestDto.DiaryRequestDto;
import com.loveyourdog.brokingservice.model.dto.requestDto.PurchaseRequestDto;
import com.loveyourdog.brokingservice.model.dto.requestDto.ReservationRequestDto;
import com.loveyourdog.brokingservice.model.dto.responseDto.BasicRequireResponseDto;
import com.loveyourdog.brokingservice.model.dto.responseDto.ReservationResponseDto;
import com.loveyourdog.brokingservice.model.entity.*;
import com.loveyourdog.brokingservice.repository.alarm.AlarmRepository;
import com.loveyourdog.brokingservice.repository.basicRequire.BasicRequireRepository;
import com.loveyourdog.brokingservice.repository.cusRequire.CusRequireRepository;
import com.loveyourdog.brokingservice.repository.customer.CustomerRespository;
import com.loveyourdog.brokingservice.repository.dogwalker.DogwalkerRepository;
import com.loveyourdog.brokingservice.repository.inquiry.InquiryRepository;
import com.loveyourdog.brokingservice.repository.offer.OfferRepository;
import com.loveyourdog.brokingservice.repository.payment.PaymentRepository;
import com.loveyourdog.brokingservice.repository.payment.PaymentTypeRepository;
import com.loveyourdog.brokingservice.repository.reservation.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
public class ReservationService {


    private final ReservationRepository reservationRepository;

    private final OfferRepository offerRepository;
    private final InquiryRepository inquiryRepository;
    private final DogwalkerRepository dogwalkerRepository;
    private final CustomerRespository customerRespository;
    private final CusRequireRepository cusRequireRepository;
    private final BasicRequireRepository basicRequireRepository;
    private final AlarmRepository alarmRepository;
    private final StorageService storageService;
    private final PaymentRepository paymentRepository;
    private final PaymentTypeRepository paymentTypeRepository;

    private final AdminService adminService;




    // info -> reservation 생성
    public boolean createReservation(ReservationRequestDto requestDto) throws Exception {

        if(requestDto.getInquiryId()==null || requestDto.getInquiryId()==0){
            if(requestDto.getOfferId()==null || requestDto.getOfferId()==0){
                // 둘다 null인 경우
                throw new NullPointerException("문의 또는 제안이 존재해야 합니다");
            } else {
                // offer가 수락되어 예약이 완료된 경우
                Offer offer = offerRepository.findById(requestDto.getOfferId()).orElseThrow(
                        ()-> new NoSuchElementException("id에 해당하는 제안이 없습니다"));
                offer.setStatus(2); // 수락을 받은 제안
                offerRepository.save(offer);
                Reservation reservation = Reservation.builder()
                        .offer(offer)
                        .status(1) // 예약 완료, 결제 전
                        .diaryStatus(1)
                        .build();
                reservationRepository.save(reservation);

                // 고객요청사항 list 생성
                List<CusRequire> cusRequires = new ArrayList<>();
                for (String cusStr :offer.getCommision().getCusRequireStrings()) {
                    CusRequire cusReq = CusRequire.builder()
                            .context(cusStr)
                            .commisionId(offer.getCommision().getId())
                            .build();
                    cusRequires.add(cusReq);
                }
                cusRequireRepository.saveAll(cusRequires);

                Alarm alarm = Alarm.builder() // 청약자에게 예약완료 알림
                        .msgType(2)
                        .dogwalker(offer.getDogwalker())
                        .reservation(reservation)
                        .checked(1)
                        .build();
                alarmRepository.save(alarm);
            }
        } else {
            if(requestDto.getOfferId()==null || requestDto.getOfferId()==0){
                // inquiry가 수락되어 예약이 완료된 경우
                Inquiry inquiry = inquiryRepository.findById(requestDto.getInquiryId()).orElseThrow(
                        ()-> new NoSuchElementException("id에 해당하는 문의가 없습니다"));
                inquiry.setStatus(2); // 수락을 받은 문의
                inquiryRepository.save(inquiry);
                Reservation reservation = Reservation.builder()
                        .inquiry(inquiry)
                        .status(1) // 예약 완료, 결제 전
                        .diaryStatus(1)
                        .build();
                reservationRepository.save(reservation);

                // 고객요청사항 list 생성
                List<CusRequire> cusRequires = new ArrayList<>();
                for (String cusStr :inquiry.getCommision().getCusRequireStrings()) {
                    CusRequire cusReq = CusRequire.builder()
                            .context(cusStr)
                            .commisionId(inquiry.getCommision().getId())
                            .build();
                    cusRequires.add(cusReq);
                }
                cusRequireRepository.saveAll(cusRequires);

                Alarm alarm = Alarm.builder() // 청약자에게 예약완료 알림
                        .msgType(2)
                        .customer(inquiry.getCustomer())
                        .reservation(reservation)
                        .checked(1)
                        .build();
                alarmRepository.save(alarm);
            } else {
                // 둘다 null이 아닌 경우
                throw new Exception("한 예약에 문의 또는 제안이 모두 존재할 수 없습니다");
            }
        }
        return true;
    }
    // info -> reservation 수정
    public boolean modifyReservation(Long reservId, ReservationRequestDto requestDto) throws Exception {
        Dogwalker dogwalker = null;
        Customer customer = null;
        // reservation update
        Reservation reservation = reservationRepository.findById(reservId).orElseThrow(
                ()-> new NoSuchElementException("id에 해당하는 예약이 없습니다"));
        if(reservation.getInquiry()!=null){
            dogwalker = reservation.getInquiry().getDogwalker();
            customer = reservation.getInquiry().getCustomer();
        }
        if(reservation.getOffer()!=null){
            dogwalker = reservation.getOffer().getDogwalker();
            customer = reservation.getOffer().getCustomer();
        }
        if(requestDto.getStartDt()!=null){
            System.out.println("requestDto.getStartDt() : "+requestDto.getStartDt());
            reservation.setStartDt(requestDto.getStartDt());
        }
        if(requestDto.getEndDt()!=null){
            System.out.println("requestDto.getEndDt() : "+requestDto.getEndDt());
            reservation.setEndDt(requestDto.getEndDt());

            if(dogwalker!=null && customer!=null){
                dogwalker.setGoalCnt(dogwalker.getGoalCnt() + 1);
                customer.setGoalCnt(customer.getGoalCnt() + 1);
                String gradeDw = getDogwalkerGrade(dogwalker.getGoalCnt() + 1, dogwalker.getStar());
                String gradeCus = getCustomerGrade(customer.getGoalCnt() + 1, customer.getTemperture());
                dogwalker.setGrade(gradeDw);
                customer.setGrade(gradeCus);
                dogwalkerRepository.save(dogwalker);
                customerRespository.save(customer);
            }
        }
        if(StringUtils.hasText(String.valueOf(requestDto.getStatus()))){
            reservation.setStatus(requestDto.getStatus());
        }
        if(StringUtils.hasText(String.valueOf(requestDto.getCanceledAt()))){
            reservation.setCanceledAt(requestDto.getCanceledAt());
        }
        if(StringUtils.hasText(String.valueOf(requestDto.getCancelerUserType()))){
            reservation.setCancelerUserType(requestDto.getCancelerUserType());
        }
        if(StringUtils.hasText(String.valueOf(requestDto.getCanceledAt()))){
            reservation.setCanceledAt(requestDto.getCanceledAt());
        }
        if(StringUtils.hasText(String.valueOf(requestDto.getDiaryStatus()))){
            reservation.setDiaryStatus(requestDto.getDiaryStatus());
        }
        if(StringUtils.hasText(String.valueOf(requestDto.getDiaryCreatedAt()))){
            reservation.setDiaryCreatedAt(requestDto.getDiaryCreatedAt());
        }
        if(StringUtils.hasText(String.valueOf(requestDto.getDiaryUpdatedAt()))){
            reservation.setDiaryUpdatedAt(requestDto.getDiaryUpdatedAt());
        }
        if(StringUtils.hasText(String.valueOf(requestDto.getTemperture()))){
            reservation.setTemperture(requestDto.getTemperture());
        }
        reservationRepository.save(reservation);



        if(requestDto.getBasicRequireDtos()!=null) {
            List<BasicRequireDto> basicRequireDtos = requestDto.getBasicRequireDtos();
            List<BasicRequire> basicRequires = new ArrayList<>();
            // basic require create
            for (BasicRequireDto dto : basicRequireDtos) {
                BasicRequire require = BasicRequire.builder()
                        .reservation(reservation)
                        .id(dto.getBasicRequireId())
                        .keyword(dto.getKeyword())
                        .dirName(dto.getDirName())
                        .fileName(dto.getFileName())
                        .extension(dto.getExtension())
                        .typeEssential(dto.getTypeEssential())
                        .build();
                basicRequires.add(require);
            }
            basicRequireRepository.saveAll(basicRequires);
        }


        // cus require update
        if(requestDto.getCusRequireDtos()!=null) {
            List<CusRequireDto> cusRequireDtos = requestDto.getCusRequireDtos();

            for (CusRequireDto dto : cusRequireDtos) {
                CusRequire cusRequire = cusRequireRepository.findById(dto.getId()).orElseThrow(
                        () -> new NoSuchElementException("id에 해당하는 고객요청이 없습니다"));
                if (dto.getDirName() != null) {
                    cusRequire.setDirName(dto.getDirName());
                }
                if (dto.getFileName() != null) {
                    cusRequire.setFileName(dto.getFileName());
                }
                if (dto.getExtension() != null) {
                    cusRequire.setExtension(dto.getExtension());
                }
                cusRequireRepository.save(cusRequire);
            }
        }
        return true;
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

    public List<ReservationResponseDto> getReservations() throws Exception {
        List<ReservationResponseDto> responseDtos = new ArrayList<>();
        List<Reservation> reservations = reservationRepository.findAll();
        for (Reservation reservation : reservations){

            if(reservation.getInquiry()==null){
                if(reservation.getOffer()==null){
                    // 둘다 null인 경우
                    throw new NullPointerException("문의 또는 제안이 존재해야 합니다");
                } else {
                    // offer가 수락되어 예약이 완료된 경우
                    Offer offer = reservation.getOffer();
                    Application application  = offer.getApplication();
                    Commision commision = offer.getCommision();
                    Dogwalker dogwalker = application.getDogwalker();
                    Customer customer = commision.getCustomer();
                    ReservationResponseDto responseDto =  ReservationResponseDto.builder()
                            .applicationId(application.getId())
                            .commisionId(commision.getId())
                            .offerId(offer.getId())
                            .reservationId(reservation.getId())
                            .status(reservation.getStatus())
//                            .startDt(reservation.getStartDt())
//                            .endDt(reservation.getEndDt())
//                            .cancelerUserType(reservation.getCancelerId())
//                            .canceledAt(reservation.getCanceledAt())
//                            .diaryCreatedAt(reservation.getDiaryCreatedAt())
//                            .diaryUpdatedAt(reservation.getDiaryUpdatedAt())
//                            .diaryStatus(reservation.getDiaryStatus())
//                            // 산책일기 관련 추가
//                            .cusDirName(customer.getDirName())
//                            .cusFileName(customer.getFileName())
//                            .cusExtension(customer.getExtension())
//                            .dwDirName(dogwalker.getDirName())
//                            .dwFileName(dogwalker.getFileName())
//                            .dwExtension(dogwalker.getExtension())
                            .build();
                    responseDtos.add(responseDto);
                }
            } else {
                // inquiry가 수락되어 예약이 완료된 경우
                if(reservation.getOffer()==null){
                    Inquiry inquiry = reservation.getInquiry();
                    Application application  = inquiry.getApplication();
                    Commision commision = inquiry.getCommision();
                    Dogwalker dogwalker = application.getDogwalker();
                    Customer customer = commision.getCustomer();
                    ReservationResponseDto responseDto  =  ReservationResponseDto.builder()
                            .applicationId(application.getId())
                            .commisionId(commision.getId())
                            .inquiryId(inquiry.getId())
                            .reservationId(reservation.getId())
                            .status(reservation.getStatus())
//                            .startDt(reservation.getStartDt())
//                            .endDt(reservation.getEndDt())
//                            .cancelerUserType(reservation.getCancelerId())
//                            .canceledAt(reservation.getCanceledAt())
//                            .diaryCreatedAt(reservation.getDiaryCreatedAt())
//                            .diaryUpdatedAt(reservation.getDiaryUpdatedAt())
//                            .diaryStatus(reservation.getDiaryStatus())
//                            // 산책일기 관련 추가
//                            .cusDirName(customer.getDirName())
//                            .cusFileName(customer.getFileName())
//                            .cusExtension(customer.getExtension())
//                            .dwDirName(dogwalker.getDirName())
//                            .dwFileName(dogwalker.getFileName())
//                            .dwExtension(dogwalker.getExtension())
                            .build();
                    responseDtos.add(responseDto);
                } else {
                    // 둘다 null이 아닌 경우
                    throw new Exception("한 예약에 문의 또는 제안이 모두 존재할 수 없습니다");
                }
            }
        }
        return responseDtos;


    }
    public ReservationResponseDto getReservationById(Long id) throws Exception {
        ReservationResponseDto responseDto = null;
        Reservation reservation = reservationRepository.findById(id).orElseThrow(
                ()-> new NoSuchElementException("id에 해당하는 예약이 없습니다"));
        
        if(reservation.getInquiry()==null){
            if(reservation.getOffer()==null){
                // 둘다 null인 경우
                throw new NullPointerException("문의 또는 제안이 존재해야 합니다");
            } else {
                // offer가 수락되어 예약이 완료된 경우
                Offer offer = reservation.getOffer();
                Application application  = offer.getApplication();
                Commision commision = offer.getCommision();
                Dogwalker dogwalker = application.getDogwalker();
                Customer customer = commision.getCustomer();
                responseDto =  ReservationResponseDto.builder()
                        .applicationDto(application.toApplicationDto(application))
                        .commisionDto(commision.toCommisionDto(commision))
                        .price(offer.getPrice())
                        .status(reservation.getStatus())
                        .reservationId(reservation.getId())
                        .startDt(reservation.getStartDt())
                        .endDt(reservation.getEndDt())
                        .cancelerUserType(reservation.getCancelerId())
                        .canceledAt(reservation.getCanceledAt())
                        .reservCreatedAt(reservation.getCreatedDate())
                        .diaryCreatedAt(reservation.getDiaryCreatedAt())
                        .diaryUpdatedAt(reservation.getDiaryUpdatedAt())
                        .diaryStatus(reservation.getDiaryStatus())
                        .temperture(reservation.getTemperture())
                        // 산책일기 관련 추가
                        .cusDirName(customer.getDirName())
                        .cusFileName(customer.getFileName())
                        .cusExtension(customer.getExtension())
                        .dwDirName(dogwalker.getDirName())
                        .dwFileName(dogwalker.getFileName())
                        .dwExtension(dogwalker.getExtension())
                        .build();
                if(reservation.getReview()==null){
                    responseDto.setReviewWriten(false);
                } else {
                    responseDto.setReviewWriten(true);
                    responseDto.setReviewId(reservation.getReview().getId());
                }
                return responseDto;
            }
        } else {
            // inquiry가 수락되어 예약이 완료된 경우
            if(reservation.getOffer()==null){
                Inquiry inquiry = reservation.getInquiry();
                Application application  = inquiry.getApplication();
                Commision commision = inquiry.getCommision();
                Dogwalker dogwalker = application.getDogwalker();
                Customer customer = commision.getCustomer();
                responseDto =  ReservationResponseDto.builder()
                        .applicationDto(application.toApplicationDto(application))
                        .commisionDto(commision.toCommisionDto(commision))
                        .price(inquiry.getPrice())
                        .status(reservation.getStatus())
                        .reservationId(reservation.getId())
                        .reservCreatedAt(reservation.getCreatedDate())
                        .startDt(reservation.getStartDt())
                        .endDt(reservation.getEndDt())
                        .cancelerUserType(reservation.getCancelerId())
                        .canceledAt(reservation.getCanceledAt())
                        .diaryCreatedAt(reservation.getDiaryCreatedAt())
                        .diaryUpdatedAt(reservation.getDiaryUpdatedAt())
                        .diaryStatus(reservation.getDiaryStatus())
                        .temperture(reservation.getTemperture())
                        // 산책일기 관련 추가
                        .cusDirName(customer.getDirName())
                        .cusFileName(customer.getFileName())
                        .cusExtension(customer.getExtension())
                        .dwDirName(dogwalker.getDirName())
                        .dwFileName(dogwalker.getFileName())
                        .dwExtension(dogwalker.getExtension())
                        .build();
                if(reservation.getReview()==null){
                    responseDto.setReviewWriten(false);
                } else {
                    responseDto.setReviewWriten(true);
                    responseDto.setReviewId(reservation.getReview().getId());

                }
                return responseDto;
            } else {
                // 둘다 null이 아닌 경우
                throw new Exception("한 예약에 문의 또는 제안이 모두 존재할 수 없습니다");
            }
        }

    }
    public List<ReservationResponseDto> getReservationsByDwId(Long dwId){


        List<ReservationResponseDto> dtos = new ArrayList<>();
        Dogwalker dogwalker = dogwalkerRepository.findById(dwId).orElseThrow(
                ()-> new NoSuchElementException("id에 해당하는 도그워커가 없습니다"));

        if(dogwalker.getOffers()!=null && dogwalker.getOffers().size() > 0 ){
            List<Offer> offers = dogwalker.getOffers();
            for (Offer o: offers) {
                if(o.getStatus()==2 ){ // 문의 상태가 예약완료 상태
                    System.out.println("예약완료상태입니다 reservation Id >> "+o.getReservation().getId());
                    dtos.add(o.getReservation().toResponseDto("offer"));
                } else {
                    System.out.println("예약완료상태가 아닙니다 offer status >> "+o.getStatus());
                }
            }



        }
        if(dogwalker.getInquiries()!=null && dogwalker.getInquiries().size() > 0){

            List<Inquiry> inquiries = dogwalker.getInquiries();
            for (Inquiry i: inquiries) {
                if(i.getStatus()==2 ){ // 문의 상태가 예약완료 상태
                    System.out.println("예약완료상태입니다 reservation Id >> "+i.getReservation().getId());
                    dtos.add(i.getReservation().toResponseDto("inquiry"));
                } else {
                    System.out.println("예약완료상태가 아닙니다 inquiry status >> "+i.getStatus());
                }
            }
        }
        return dtos;
    }

    public List<ReservationResponseDto> getReservationsByDwIdAndStatus(Long dwId, int status){


        List<ReservationResponseDto> dtos = new ArrayList<>();
        Dogwalker dogwalker = dogwalkerRepository.findById(dwId).orElseThrow(
                ()-> new NoSuchElementException("id에 해당하는 도그워커가 없습니다"));


        if(dogwalker.getInquiries()!=null && dogwalker.getInquiries().size() > 0){
            List<Inquiry> inquiries = dogwalker.getInquiries();
            for (Inquiry i: inquiries) {
//                if(i.getStatus()==2 && i.getReservation()!=null){ // 문의 상태가 예약완료 상태
                if(i.getReservation()!=null){ // 문의 상태가 예약완료 상태
//                    System.out.println("예약완료상태입니다 reservation Id >> "+i.getReservation().getId());
                    Reservation reservation = i.getReservation();
                    if(reservation.getStatus()==status){
                        dtos.add(reservation.toResponseDto("inquiry"));
                    } else {
                        System.out.println("예약 상태가 일치하지 않습니다");
                    }
                } else {
                    System.out.println("예약완료상태가 아닙니다 inquiry status >> "+i.getStatus());
                }
            }
        }
        if(dogwalker.getOffers()!=null && dogwalker.getOffers().size() > 0){
            List<Offer> offers = dogwalker.getOffers();
            for (Offer o: offers) {
//                if(o.getStatus()==2 && o.getReservation()!=null){ // 문의 상태가 예약완료 상태
                if(o.getReservation()!=null){ // 문의 상태가 예약완료 상태
                    System.out.println("예약완료상태입니다 reservation Id >> "+o.getReservation().getId());
                    Reservation reservation = o.getReservation();
                    if(reservation.getStatus()==status){
                        dtos.add(reservation.toResponseDto("offer"));
                    } else {
                        System.out.println("예약 상태가 일치하지 않습니다");
                    }

                } else {
                    System.out.println("예약완료상태가 아닙니다 offer status >> "+o.getStatus());
                }
            }
        }
        return dtos;
    }

    public List<ReservationResponseDto> getReservationsByCusId(Long cusId){


        List<ReservationResponseDto> dtos = new ArrayList<>();
        Customer customer = customerRespository.findById(cusId).orElseThrow(
                ()-> new NoSuchElementException("id에 해당하는 고객이 없습니다"));

        if(customer.getInquiries()!=null && customer.getInquiries().size() > 0){
            List<Inquiry> inquiries = customer.getInquiries();
            for (Inquiry i: inquiries) {
                if(i.getStatus()==2 && i.getReservation()!=null){ // 문의 상태가 예약완료 상태
                    System.out.println("예약완료상태입니다 reservation Id >> "+i.getReservation().getId());
                    dtos.add(i.getReservation().toResponseDto("inquiry"));
                } else {
                    System.out.println("예약완료상태가 아닙니다 inquiry status >> "+i.getStatus());
                }
            }
        }
        if(customer.getOffers()!=null && customer.getOffers().size() > 0){
            List<Offer> offers = customer.getOffers();
            for (Offer o: offers) {
                if(o.getStatus()==2 && o.getReservation()!=null){ // 문의 상태가 예약완료 상태
                    System.out.println("예약완료상태입니다 reservation Id >> "+o.getReservation().getId());
                    dtos.add(o.getReservation().toResponseDto("offer"));
                } else {
                    System.out.println("예약완료상태가 아닙니다 offer status >> "+o.getStatus());
                }
            }
        }
        return dtos;
    }
    public List<ReservationResponseDto> getReservationsByCusIdAndStatus(Long cusId, int status){


        List<ReservationResponseDto> dtos = new ArrayList<>();
        Customer customer = customerRespository.findById(cusId).orElseThrow(
                ()-> new NoSuchElementException("id에 해당하는 도그워커가 없습니다"));


        if(customer.getOffers()!=null && customer.getOffers().size() > 0){
            List<Offer> offers = customer.getOffers();
            for (Offer o: offers) {
                if(o.getStatus()==2 && o.getReservation()!=null){ // 문의 상태가 예약완료 상태
                    System.out.println("예약완료상태입니다 reservation Id >> "+o.getReservation().getId());
                    Reservation reservation = o.getReservation();
                    if(reservation.getStatus()==status){
                        dtos.add(reservation.toResponseDto("offer"));
                    } else {
                        System.out.println("예약 상태가 일치하지 않습니다");
                    }

                } else {
                    System.out.println("예약완료상태가 아닙니다 offer status >> "+o.getStatus());
                }
            }
        }
        if(customer.getInquiries()!=null && customer.getInquiries().size() > 0){
            List<Inquiry> inquiries = customer.getInquiries();
            for (Inquiry i: inquiries) {
                if(i.getStatus()==2 && i.getReservation()!=null){ // 문의 상태가 예약완료 상태
//                    System.out.println("예약완료상태입니다 reservation Id >> "+i.getReservation().getId());
                    Reservation reservation = i.getReservation();
                    if(reservation.getStatus()==status){
                        dtos.add(reservation.toResponseDto("inquiry"));
                    } else {
                        System.out.println("예약 상태가 일치하지 않습니다");
                    }
                } else {
                    System.out.println("예약완료상태가 아닙니다 inquiry status >> "+i.getStatus());
                }
            }
        }
        return dtos;
    }

    public List<BasicRequireResponseDto> getBasicRequiresByReservId(Long reservId){
        List<BasicRequireResponseDto> dtos = new ArrayList<>();
        Reservation reservation = reservationRepository.findById(reservId).orElseThrow(
                ()-> new NoSuchElementException("id에 해당하는 예약이 없습니다"));

        for (BasicRequire require : reservation.getBasicRequires()) {
            dtos.add(require.toDto());
        }

        return dtos;
    }

    public List<CusRequireDto> getCusRequiresByReservId(Long reservId){
        Reservation reservation = reservationRepository.findById(reservId).orElseThrow(
                ()-> new NoSuchElementException("id에 해당하는 예약이 없습니다"));

        List<CusRequireDto> dtos = new ArrayList<>();
        if(reservation.getInquiry()!=null){
            for (CusRequire require : reservation.getCusRequires()) {
                dtos.add(require.toCusRequireDto());
            }
        } else  if(reservation.getOffer()!=null){
            for (CusRequire require : reservation.getCusRequires()) {
                dtos.add(require.toCusRequireDto());
            }
        } else {
            throw new NullPointerException("inquiry, offer 둘 다 없습니다.");
        }
        return dtos;
    }



    public String createDiary(List<MultipartFile> filesB,
                              List<MultipartFile> filesC,
                              DiaryRequestDto requestDto) throws Exception {

        Reservation reservation = reservationRepository.findById(requestDto.getReservationId()).orElseThrow(
                ()-> new NoSuchElementException("id에 해당하는 reservation이 없습니다"));

        // Reservation의 산책 관련 데이터 업데이트
        if(requestDto.getDiaryCreatedAt()!=null){
            reservation.setDiaryCreatedAt(requestDto.getDiaryCreatedAt()); // 산책일지 생성일
        }
        if(requestDto.getDiaryUpdatedAt()!=null){
            reservation.setDiaryUpdatedAt(requestDto.getDiaryUpdatedAt()); // 산책일지 수정일
        }
        if(requestDto.getDiaryStatus() != 0){
            reservation.setDiaryStatus(requestDto.getDiaryStatus()); // 산책일지 작성상태
        }
        reservation.setTemperture(requestDto.getTemperture()); // 고객 매너온도

        reservationRepository.save(reservation);


        // Basic require 생성
        List<BasicRequire> basicRequires = new ArrayList<>();
        if(filesB!=null && filesB.size()>0) {
            if (filesB.size() == requestDto.getKeywords().size()) {
                // s3에 저장
                List<Image> imagesB = storageService.uploadToS3("basicRequire", filesB);
                // DB에 저장
                for (int i = 0; i < requestDto.getKeywords().size(); i++) {
                    BasicRequire basicRequire = BasicRequire.builder()
                            .keyword(requestDto.getKeywords().get(i))
                            .dirName(imagesB.get(i).getDirName())
                            .fileName(imagesB.get(i).getFileName())
                            .extension(imagesB.get(i).getExtension())
                            .typeEssential(keywordToTypeEssential(requestDto.getKeywords().get(i)))
                            .reservation(reservation)
                            .build();
                    basicRequires.add(basicRequire);
                }
                basicRequireRepository.saveAll(basicRequires);
            } else {
                throw new Exception("imagesB.size()!=requestDto.getKeywords().size()");
            }
        }


        // cus require 이미지 넣기
        List<CusRequire> cusRequires = new ArrayList<>();
        if(filesC!=null && filesC.size()>0) {
            if (filesC.size() == requestDto.getCusRequireIds().size()) {
                // s3에 저장
                List<Image> imagesC = storageService.uploadToS3("cusRequire", filesC);
                // DB에 저장
                for (int i = 0; i < requestDto.getCusRequireIds().size(); i++) {
                    CusRequire cusRequire = cusRequireRepository.findById(requestDto.getCusRequireIds().get(i)).orElseThrow(() -> {
                        throw new NoSuchElementException("id에 해당하는 cusRequire이 없습니다");
                    });
                    cusRequire.setDirName(imagesC.get(i).getDirName());
                    cusRequire.setFileName(imagesC.get(i).getFileName());
                    cusRequire.setExtension(imagesC.get(i).getExtension());
                    cusRequires.add(cusRequire);
                }

                cusRequireRepository.saveAll(cusRequires);
            } else {
                throw new Exception("imagesC.size()!=requestDto.getCusRequireIds().size()");
            }
        }

        // 고객 평균 매너온도 갱신
        Customer customer = null;
        if(reservation.getInquiry()!=null){
            customer =  reservation.getInquiry().getCustomer();
        } else if(reservation.getOffer()!=null){
            customer =  reservation.getOffer().getCustomer();
        }
        int newTemp = adminService.getAverageStar(customer.getTemperture(), // 기존 star
                customer.getGoalCnt()-1, // 기존 goalcnt
                requestDto.getTemperture());
        customer.setTemperture(newTemp);
        String gradeCus = getCustomerGrade(customer.getGoalCnt(), newTemp);
        customer.setGrade(gradeCus);
        customerRespository.save(customer);

        return "true";
    }

    public int keywordToTypeEssential(String keyword){
        if(keyword.equalsIgnoreCase("cmc") || keyword.equalsIgnoreCase("rop")){
            return 1;
        } else if(keyword.equalsIgnoreCase("dor") || keyword.equalsIgnoreCase("rea")){
            return 2;
        } else if(keyword.equalsIgnoreCase("rod") || keyword.equalsIgnoreCase("pac") || keyword.equalsIgnoreCase("rel")){
            return 3;
        } else if(keyword.equalsIgnoreCase("bak") || keyword.equalsIgnoreCase("rpb")){
            return 4;
        } else {
            return 0;
        }
    }

    public List<ReservationResponseDto> getDiariesByCusId(Long cusId){
        List<ReservationResponseDto> dtos = new ArrayList<>();
        List<Reservation> reservations1 = new ArrayList<>();
        List<Reservation> reservations2 = new ArrayList<>();
        Customer customer = customerRespository.findById(cusId).orElseThrow(
                ()-> new NoSuchElementException("id에 해당하는 고객이 없습니다"));

        System.out.println("1");
        if(customer.getInquiries()!=null && customer.getInquiries().size() > 0){
        System.out.println("2");
            List<Inquiry> inquiries = customer.getInquiries();
            for (Inquiry i: inquiries) {
                if(i.getStatus()==2 && i.getReservation()!=null){ // 문의 상태가 예약완료 상태
        System.out.println("3");
                    Reservation reservation = i.getReservation();
                    if(reservation.getDiaryCreatedAt()!=null && reservation.getStartDt()!=null){ // 산책일지 생성 후, 산책 시작 후
        System.out.println("4");
                        if(reservation.getDiaryStatus()==2 || reservation.getDiaryStatus()==3){ // 일부분 or 전부 작성 상태
        System.out.println("5");
                            reservations1.add(reservation);
                        }
                    }
                }
            }
            for (Reservation reservation :reservations1) {
                System.out.println("6");
                dtos.add(reservation.toResponseDto("inquiry"));
            }

        }
        System.out.println("가");
        if(customer.getOffers()!=null && customer.getOffers().size() > 0){
            List<Offer> offers = customer.getOffers();
            for (Offer o: offers) {
        System.out.println("다");
                if(o.getStatus()==2 && o.getReservation()!=null){ // 문의 상태가 예약완료 상태
                    Reservation reservation = o.getReservation();
        System.out.println("라");
                    if(reservation.getDiaryCreatedAt()!=null && reservation.getStartDt()!=null){ // 산책일지 생성 후, 산책 시작 후
                        if(reservation.getDiaryStatus()==2 || reservation.getDiaryStatus()==3){ // 일부분 or 전부 작성 상태
                            reservations2.add(reservation);
                        }
                    }
                }
            }
            for (Reservation reservation :reservations2) {
                dtos.add(reservation.toResponseDto("offer"));
            }
        }
        return dtos;

    }
    @Transactional // 하나라도 에러나면 롤백
    public boolean purchase(PurchaseRequestDto requestDto){
        Reservation reservation = reservationRepository.findById(requestDto.getReservationId()).orElseThrow(
                ()-> new NoSuchElementException("id에 해당하는 reserv이 없습니다"));
        PaymentType paymentType = paymentTypeRepository.findById(requestDto.getPaymentTypeId()).orElseThrow(
                ()-> new NoSuchElementException("id에 해당하는 reserv이 없습니다"));
        Customer customer = null;
        if(reservation.getInquiry()!=null){
            customer = reservation.getInquiry().getCustomer();
        } else if(reservation.getOffer()!=null) {
            customer = reservation.getOffer().getCustomer();
        }




        // 예약상태변경
        reservation.setStatus(2); // 결제완료 산책 전
        reservationRepository.save(reservation);

        // Payment 생성
        Payment payment = Payment.builder()
                .paymentType(paymentType) // 결제유형
                .reservation(reservation) // 예약
                // 주문자 정보
                .customer(customer)
                .name(requestDto.getBuyer_name())
                .phone(requestDto.getBuyer_tel())
                .email(requestDto.getBuyer_email())
                // 결제금액
                .totPrice(requestDto.getAmount())
                // iamport
                .pg(requestDto.getPg())
                .payMethod(requestDto.getPay_method())
                .merchant_uid(requestDto.getMerchant_uid())
                .requestId(requestDto.getRequest_id())
                .build();
        if(requestDto.getPaymentTypeId()==1){ // 카드결제
//           payment.setCardCompany(requestDto.getCardCompany());
//           payment.setCardNumber(requestDto.getCardNumber());
//           payment.setExpirationMonth(requestDto.getExpirationMonth());
//           payment.setExpirationYear(requestDto.getExpirationYear());
//           payment.setSecretCode(requestDto.getSecretCode());
        } else if(requestDto.getPaymentTypeId()==2){ // 모바일결제

        } else if(requestDto.getPaymentTypeId()==3){ // 카카오페이

        }
        paymentRepository.save(payment);
        return true;
    }

}
