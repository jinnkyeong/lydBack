package com.loveyourdog.brokingservice.model.dto.responseDto;

import com.loveyourdog.brokingservice.model.dto.querydsl.LocationDto;
import com.loveyourdog.brokingservice.model.enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileResponseDto {

    private Long userId;
    private String userType;
    private String nick;
    private String phone;
    private String email;
    private String pwd;
    private boolean passed; // 도그워커만

    private String dirName;
    private String fileName;
    private String extension;

    private int testScore;


    private int star;
    private int temperture;
    private String profileMessage;
    private String grade;
    private int monthGoalCnt;
    private int goalCnt;


}
