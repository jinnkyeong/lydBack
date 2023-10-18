package com.loveyourdog.brokingservice.model.dto.responseDto;

import com.loveyourdog.brokingservice.model.dto.querydsl.CusRequireDto;
import com.loveyourdog.brokingservice.model.entity.*;
import com.loveyourdog.brokingservice.model.enums.AccountType;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
//@Builder
public class CommisionResponseDto {

    private Long commisionId;
    private Long customerId;

    private AccountType accountType; // 계정 유형
    private String email; // 필요
    private String name;
    private int age;
    private String gen; // m or f
    private String phone;
    private String nick;
    private String addrState;
    private String addrTown;
    private String addrDetail;
    private int birthYear;
    private int temperture;


    private String price; // 희망이용금액
    private int month; // 산책일자 월
    private int day; // 산책일자 일
    private int startHour; // 산책시작 시
    private int startMin; // 산책시작 분
    private int endHour; // 산책종료 시
    private int endMin; // 산책종료 분

    private String dogName;
    private int dogAge;
    private int dogWeight;
    private String dogType;
    private String breed;
    private String dogAggr; // 공격성 1~5
    private String dogHealth; // 건강상태 1~5
    private String road; // 지정산책로

    private LocalDateTime createdAt;

    private List<CusRequireDto> cusRequireDtos;
//    private List<String> cusRequires;
    private List<String> certificateKeywords;

    private String dirName;
    private String fileName;
    private String extension;




    public CommisionResponseDto(Commision commision){
        // 일단 전부...
        String aggr = null;
        switch (commision.getDogAggr()){
            case 1 : aggr = "매우 양호";
                break;
            case 2 : aggr = "약간 양호";
                break;
            case 3 : aggr = "보통";
                break;
            case 4 : aggr = "약간 심함";
                break;
            case 5 : aggr = "매우 심함";
                break;
            default:
                break;
        }
        String  health = null;
        switch (commision.getDogHealth()){
            case 1 : health = "매우 나쁨";
                break;
            case 2 : health = "약간 나쁨";
                break;
            case 3 : health = "보통";
                break;
            case 4 : health = "약간 좋음";
                break;
            case 5 : health = "매우 좋음";
                break;
            default:
                break;
        }
        String  dogType = null;
        switch (commision.getDogType()){
            case 1 : dogType = "소형견";
                break;
            case 2 : dogType = "중형견";
                break;
            case 3 : dogType = "대형견";
                break;
            case 4 : dogType = "초대형견";
                break;
        }


        this.temperture = commision.getCustomer().getTemperture();
        this.commisionId = commision.getId();
        this.customerId = commision.getCustomer().getId();
        this.accountType = commision.getCustomer().getAccountType();
        this.email = commision.getCustomer().getEmail();
        this.name = commision.getCustomer().getName();
        this.age = LocalDate.now().getYear() - commision.getCustomer().getBirthYear();
        this.gen = commision.getCustomer().getGen();
        this.phone = commision.getCustomer().getPhone();
        this.nick = commision.getCustomer().getNick();
        this.addrState = commision.getCustomer().getAddrState();
        this.addrTown = commision.getCustomer().getAddrTown();
        this.addrDetail = commision.getCustomer().getAddrDetail();
        this.birthYear = commision.getCustomer().getBirthYear();

        this.price = ""+commision.getPrice();
        this.month = commision.getMonth();
        this.day = commision.getDay();
        this.startHour = commision.getStartHour();
        this.startMin = commision.getStartMin();
        this.endHour = commision.getEndHour();
        this.endMin = commision.getEndMin();



        this.dogName = commision.getDogName();
        this.dogAge = commision.getDogAge();
        this.dogWeight = commision.getDogWeight();
        this.dogType = dogType;
        this.breed = commision.getBreed();
        this.dogAggr = aggr;
        this.dogHealth = health;
        this.road = commision.getRoad();
        this.createdAt = commision.getCreatedDate();
        List<CusRequireDto> requireDtos = new ArrayList<>();
        if(commision.getCusRequireStrings().size()>0){ // 없으면 null
            for (String cr: commision.getCusRequireStrings()) {
                CusRequireDto dto = CusRequireDto.builder()
                        .context(cr)
                        .build();
                requireDtos.add(dto);
            }
            this.cusRequireDtos = requireDtos;
        }

        List<String> keywords = new ArrayList<>();
        if(commision.getCustomer().getWishCertificates().size()>0){ // 없으면 null
            for (String c :commision.getCustomer().getWishCertificates()) {
                keywords.add(c);
            }
            this.certificateKeywords = keywords;
        }
        this.dirName = commision.getCustomer().getDirName();
        this.fileName = commision.getCustomer().getFileName();
        this.extension = commision.getCustomer().getExtension();

    }
}
