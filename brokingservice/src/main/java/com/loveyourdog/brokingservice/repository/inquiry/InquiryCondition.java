package com.loveyourdog.brokingservice.repository.inquiry;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor

public class InquiryCondition {


    private int page;
    private int size;
    private List<String> orderStr;

    private Long userId;
    private String userType;
    private int key;
}