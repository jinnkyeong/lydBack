package com.loveyourdog.brokingservice.model.dto.querydsl;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
//@AllArgsConstructor
public class ApplicationDto {

    private Long applicationId;
    private String name;
    private String nick;
    private String gen;
    private int birthYear;
    private String addrState;
    private String addrTown;
    private String addrDetail;
    private List<CertificateDto> certificates;
//    private List<Certificate> certificates;
    private String sentence;
    private int price;
    private String phone;
    private String email;
    private int star;
    private int view;
    private List<WeekdayDto> weekdays;
//    private List<Weekday> weekdays;
    private String dirName;
    private String fileName;
    private String extension;
    //도그워커 입금계좌
    private String depositBank;
    private String depositAccount;


//    모든 변수가 NULL 이면 true
//    하나라도 NULL이 아닌 변수가 있으면 false
    public boolean isDtoEntireVariableNull() {
        try {
            for (Field f : getClass().getDeclaredFields()) {
                if (f.get(this) != null) {
                    return false;
                }
            }
            return true;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @QueryProjection
    public ApplicationDto(Long applicationId, String name, String nick, String gen, int birthYear, String addrState, String addrTown, String addrDetail, List<CertificateDto> certificates, String sentence, int price, String phone, String email, int star, int view, List<WeekdayDto> weekdays, String dirName, String fileName, String extension, String depositBank, String depositAccount) {
        this.applicationId = applicationId;
        this.name = name;
        this.nick = nick;
        this.gen = gen;
        this.birthYear = birthYear;
        this.addrState = addrState;
        this.addrTown = addrTown;
        this.addrDetail = addrDetail;
        this.certificates = certificates;
        this.sentence = sentence;
        this.price = price;
        this.phone = phone;
        this.email = email;
        this.star = star;
        this.view = view;
        this.weekdays = weekdays;
        this.dirName = dirName;
        this.fileName = fileName;
        this.extension = extension;
        this.depositBank = depositBank;
        this.depositAccount = depositAccount;
    }
}
