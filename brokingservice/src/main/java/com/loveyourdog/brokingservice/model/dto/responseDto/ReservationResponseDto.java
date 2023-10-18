package com.loveyourdog.brokingservice.model.dto.responseDto;

import com.loveyourdog.brokingservice.model.dto.querydsl.ApplicationDto;
import com.loveyourdog.brokingservice.model.dto.querydsl.CommisionDto;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationResponseDto {
    private Long reservationId;
    private Long applicationId;
    private Long commisionId;
    private Long offerId;
    private Long inquiryId;

    private ApplicationDto applicationDto;
    private CommisionDto commisionDto;

//    // 둘 중 하나
//    private Long inquiryId;
//    private Long offerId;
    private int status;
    // 0 : 취소된 예약
    // 1 : 예약 완료, 결제 전
    // 2 : 결제 완료, 산책 전
    // 3 : 산책 중
    // 4 : 산책 완료, 정산 전
    // 5 : 정산 중
    // 6 : 정산 완료



    private LocalDateTime startDt; // 실제 산책시작 일시
    private LocalDateTime endDt; // 실제 산책종료 일시

    private int diaryStatus;  // 1 하나도 작성X  2 일부분  3 complete(모든 사진)
    private LocalDateTime diaryCreatedAt; // 산책일지 작성일시
    private LocalDateTime diaryUpdatedAt; // 산책일지 수정일시


    @ColumnDefault("0")
    private int temperture; // 해당 산책에서 매긴 고객의 매너온도

    private LocalDateTime canceledAt; // 예약 취소 일시
    private Long cancelerId; // 예약 취소자Id
    private Long cancelerUserType; // 예약 취소자 userType

    private LocalDateTime reservCreatedAt; // 예약일자
    
    private int price; // 최종 금액

    private String cusDirName;
    private String cusFileName;
    private String cusExtension;
    private String dwDirName;
    private String dwFileName;
    private String dwExtension;


    private boolean reviewWriten;
    private Long reviewId;





}
