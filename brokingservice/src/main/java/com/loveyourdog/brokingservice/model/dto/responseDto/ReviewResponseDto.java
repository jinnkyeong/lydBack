package com.loveyourdog.brokingservice.model.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponseDto {

//
//    private Long applicationId;
//    private Long commisionId;
    private Long dogwalkerId; // 대상 도그워커

    private Long reviewId;
    private int star; // 별점(0~10)
    private String context;
    private String dirName; // S3 객체 이름
    private String fileName; // 이미지 파일 이름
    private String extension; // 파일 확장자

    private Long customerId; // 리뷰 작성자
    private String customerNick;
    private String breed;
    private int dogType;
    private int month;
    private int day;
    // enddt - startdt
    private Long hour;
    private Long min;
    private String addrState;
    private String addrTown;

    private int commentCnt;

}
