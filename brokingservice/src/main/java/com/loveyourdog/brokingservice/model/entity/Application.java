package com.loveyourdog.brokingservice.model.entity;

import com.loveyourdog.brokingservice.model.dto.querydsl.ApplicationDto;
import com.loveyourdog.brokingservice.model.dto.querydsl.CertificateDto;
import com.loveyourdog.brokingservice.model.dto.querydsl.LocationDto;
//import com.loveyourdog.brokingservice.model.dto.querydsl.WeekdayDto;
import com.loveyourdog.brokingservice.model.dto.querydsl.WeekdayDto;
import com.loveyourdog.brokingservice.model.dto.responseDto.ApplyDetailResponseDto;
import com.loveyourdog.brokingservice.model.dto.responseDto.ApplyResponseDto;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicUpdate
public class Application extends BaseTime{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 해당 엔티티 한정 identity 설정
    private Long id;




    // application(N) : dogwalker(1)
    @JoinColumn(name="dogwalker")
    @ManyToOne(fetch = FetchType.LAZY)
    private Dogwalker dogwalker;

    // application(1) : location(N)
    @OneToMany(mappedBy = "application",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    @Builder.Default
    private List<Location> locations  = new ArrayList<>(); // 근무가능 지역
    public void addLocations(List<Location> locations){
        this.locations.addAll(locations);
        locations.forEach(o -> o.setApplication(this));
    }
    public void setLocations(List<Location> locations){
        this.locations = locations;
        locations.forEach(o -> o.setApplication(this));
    }
    // application(1) : weekday(N)
    @OneToMany(mappedBy = "application",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @Builder.Default
    private List<Weekday> weekdays  = new ArrayList<>(); // 근무가능 요일
    public void addWeekdays(List<Weekday> weekdays){
        this.weekdays.addAll(weekdays);
        weekdays.forEach(o -> o.setApplication(this));
    }
    // application(1) : inquiry(N)
    @OneToMany(mappedBy = "application",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @Builder.Default
    private List<Inquiry> inquiries  = new ArrayList<>(); // 문의(from 고객)
    public void addInquiries(List<Inquiry> inquiries){
        this.inquiries.addAll(inquiries);
        inquiries.forEach(o -> o.setApplication(this));
    }

    // application(1) : offer(N)
    @OneToMany(mappedBy = "application",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @Builder.Default
    private List<Offer> offers  = new ArrayList<>(); // 문의제안(from 도그워커)
    public void addOffers(List<Offer> offers){
        this.offers.addAll(offers);
        offers.forEach(o -> o.setApplication(this));
    }






    @ColumnDefault("false")
    private boolean adopted; // 반려경험 유무

    private int adoptedWhich; // 키웠던 동물
    // 1 고양이 2 강아지 3 그외

    private int adoptedMonth; // 반려기간(개월)

    private int petType; // 희망하는 강아지 유형 1소 2중 3대 4초대

    private int price; // 희망하는 이용금액

    private String sentence; // 각오의 한마디

    @ColumnDefault("0")
    private int view;



    public ApplyDetailResponseDto toApplyDetailResponseDto(Application a){
//        List<CertificateDto> cDtos = new ArrayList<>();
//        List<WeekdayDto> wDtos = new ArrayList<>();
        List<LocationDto> lDtos = new ArrayList<>();
        List<String> weekdayKeywords = new ArrayList<>();
        List<String> certificateKeywords = new ArrayList<>();
        List<Certificate> certificateList = a.dogwalker.getCertificates();
        List<Weekday> weekdayList = a.getWeekdays();
        List<Location> locationList = a.getLocations();
        
        int age = LocalDate.now().getYear() - a.getDogwalker().getBirthYear(); // 나이=올해-생년
        // Location dto로
        for (Location l: locationList) {
            LocationDto locationDto = LocationDto.builder()
                    .id(l.getId())
                    .state(l.getState())
                    .town(l.getTown()).build();
            lDtos.add(locationDto);
        }
        // Certificate keyword만
        for (Certificate c : certificateList) {
            certificateKeywords.add(c.getKeyword());
        }
        // Weekday day만
        for (Weekday w: weekdayList) {
            weekdayKeywords.add(w.getDay());
        }

        ApplyDetailResponseDto dto = ApplyDetailResponseDto.builder()

                .star(a.getDogwalker().getStar()) // 평균별점
                .dogwalkerId(a.getDogwalker().getId())
                .applicationId(a.getId())
                .age(age) // 생년으로 구함
                .accountType(a.getDogwalker().getAccountType())
                .email(a.getDogwalker().getEmail())
                .name(a.getDogwalker().getName())
                .birthYear(a.getDogwalker().getBirthYear())
                .gen(a.getDogwalker().getGen())
                .phone(a.getDogwalker().getPhone())
                .nick(a.getDogwalker().getNick())
                .addrState(a.getDogwalker().getAddrState())
                .addrTown(a.getDogwalker().getAddrTown())
                .addrDetail(a.getDogwalker().getAddrDetail())
                .interviewPassed(a.getDogwalker().isInterviewPassed())
                .appicationPassed(a.getDogwalker().isAppicationPassed())
                .passed(a.getDogwalker().isPassed())
                .depositBank(a.getDogwalker().getDepositBank())
                .depositAccount(a.getDogwalker().getDepositAccount())
                .dirName(a.getDogwalker().getDirName())
                .fileName(a.getDogwalker().getFileName())
                .extension(a.getDogwalker().getExtension())
//                .locations(lDtos)
//                .weekdayKeywords(weekdayKeywords) // 단순 List<String>로 변경해봄
//                .certificateKeywords(certificateKeywords) // 단순 List<String>로 변경해봄
                .adopted(a.isAdopted())
                .adoptedWhich(String.valueOf(a.getAdoptedWhich()))
                .adoptedMonth(a.getAdoptedMonth())
                .petType(String.valueOf(a.getPetType()))
                .price(a.getPrice())
                .sentence(a.getSentence())
                .build();
        // 없을때 null로 처리..(vue에서 없을 때 처리하기 위함)
        if(lDtos.size()>0){
            dto.setLocations(lDtos);
        }
        if(weekdayKeywords.size()>0){
            dto.setWeekdayKeywords(weekdayKeywords);
        }
        if(certificateKeywords.size()>0){
            dto.setCertificateKeywords(certificateKeywords);
        }
        return dto;
    }





    public ApplyResponseDto toApplyResponseDto(Application a){
        List<CertificateDto> cDtos = new ArrayList<>();
        List<WeekdayDto> wDtos = new ArrayList<>();
        List<LocationDto> lDtos = new ArrayList<>();
        List<Certificate> certificateList = a.dogwalker.getCertificates();
        List<Weekday> weekdayList = a.getWeekdays();
        List<Location> locationList = a.getLocations();

        //  <List>자격증-> <List>자격증DTO
        for (Certificate c : certificateList) {
            CertificateDto certificateDto = CertificateDto.builder()
                    .id(c.getId())
                    .keyword(c.getKeyword())
                    .build();
            cDtos.add(certificateDto);
        }
        //  <List>요일-> <List>요일DTO
        for (Weekday w: weekdayList) {
            WeekdayDto weekdayDto = WeekdayDto.builder()
                    .id(w.getId())
                    .day(w.getDay()).build();
            wDtos.add(weekdayDto);
        }
        //  <List>장소-> <List>장소DTO
        for (Location l: locationList) {
            LocationDto locationDto = LocationDto.builder()
                    .id(l.getId())
                    .state(l.getState())
                    .town(l.getTown()).build();
            lDtos.add(locationDto);
        }
        ApplyResponseDto dto = ApplyResponseDto.builder()
                .dogwalkerId(a.getDogwalker().getId())
                .applicationId(a.getId())
                .accountType(a.getDogwalker().getAccountType())
                .email(a.getDogwalker().getEmail())
                .name(a.getDogwalker().getName())
                .birthYear(a.getDogwalker().getBirthYear())
                .gen(a.getDogwalker().getGen())
                .phone(a.getDogwalker().getPhone())
                .nick(a.getDogwalker().getNick())
                .addrState(a.getDogwalker().getAddrState())
                .addrTown(a.getDogwalker().getAddrTown())
                .addrDetail(a.getDogwalker().getAddrDetail())
                .interviewPassed(a.getDogwalker().isInterviewPassed())
                .appicationPassed(a.getDogwalker().isAppicationPassed())
                .passed(a.getDogwalker().isPassed())
                .depositBank(a.getDogwalker().getDepositBank())
                .depositAccount(a.getDogwalker().getDepositAccount())
                .dirName(a.getDogwalker().getDirName())
                .fileName(a.getDogwalker().getFileName())
                .extension(a.getDogwalker().getExtension())
                .view(a.getView())
                .locations(lDtos)
                .weekdays(wDtos)
                .certificates(cDtos)
                .adopted(a.isAdopted())
                .adoptedWhich(a.getAdoptedWhich())
                .adoptedMonth(a.getAdoptedMonth())
                .petType(a.getPetType())
                .price(a.getPrice())
                .sentence(a.getSentence())
                .createdAt(a.getCreatedDate())
                .updatedAt(a.getModifiedDate())
                .build();
        return dto;
    }





    public static ApplicationDto toApplicationDto(Application application){
        // 자격증list -> 자격증dtolist
        // 이걸 지원서dto에 넣기
        List<CertificateDto> cDtos = new ArrayList<>();
        List<WeekdayDto> wDtos = new ArrayList<>();
        Optional<List<Certificate>> certificates = Optional.ofNullable(application.getDogwalker().getCertificates());
        if(certificates.isPresent()) {
            for (Certificate c : application.getDogwalker().getCertificates()) {
                CertificateDto certificateDto = CertificateDto.builder()
                        .id(c.getId())
                        .keyword(c.getKeyword())
                        .build();
                cDtos.add(certificateDto);
            }
        }


        if(application.getWeekdays()!=null){
            List<Weekday> weekdayList = application.getWeekdays();
            for (Weekday w: weekdayList) {
                WeekdayDto weekdayDto = WeekdayDto.builder()
                        .id(w.getId())
                        .day(w.getDay()).build();
                wDtos.add(weekdayDto);
            }
        }
        ApplicationDto applicationDto = ApplicationDto.builder()
                .applicationId(application.getId())
                .name(application.getDogwalker().getName())
                .nick(application.getDogwalker().getNick())
                .gen(application.getDogwalker().getGen())
                .birthYear(application.getDogwalker().getBirthYear())
                .addrState(application.getDogwalker().getAddrState())
                .addrTown(application.getDogwalker().getAddrTown())
                .addrDetail(application.getDogwalker().getAddrDetail())
                .certificates(cDtos)
                .sentence(application.getSentence())
                .price(application.getPrice())
                .phone(application.getDogwalker().getPhone())
                .email(application.getDogwalker().getEmail())
                .star(application.getDogwalker().getStar())
                .view(application.getView())
                .weekdays(wDtos)
                .dirName(application.getDogwalker().getDirName())
                .fileName(application.getDogwalker().getFileName())
                .extension(application.getDogwalker().getExtension())
                .depositBank(application.getDogwalker().getDepositBank())
                .depositAccount(application.getDogwalker().getDepositAccount())
                .build();
        return applicationDto;
    }

}
