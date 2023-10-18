package com.loveyourdog.brokingservice.model.dto.requestDto;

import com.loveyourdog.brokingservice.model.dto.querydsl.BasicRequireDto;
import com.loveyourdog.brokingservice.model.dto.querydsl.CusRequireDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequestDto {


    private Long reservationId;
    private int star;
    private String context;
//    private String dirName; // S3 객체 이름
//    private String fileName; // 이미지 파일 이름
//    private String extension; // 파일 확장자



}
