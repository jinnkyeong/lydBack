package com.loveyourdog.brokingservice.model.dto.requestDto;

import com.loveyourdog.brokingservice.model.dto.querydsl.BasicRequireDto;
import com.loveyourdog.brokingservice.model.dto.querydsl.CusRequireDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequestDto {

    // 둘 중 하나
    private Long inquiryId;
    private Long offerId;

    private int status;
    // 0 : 취소된 예약
    // 1 : 예약 완료, 결제 전
    // 2 : 결제 완료, 산책 전
    // 3 : 산책 중
    // 4 : 산책 완료, 정산 전
    // 5 : 정산 중
    // 6 : 정산 완료

    private LocalDateTime canceledAt; // 예약 취소 일시
    private Long cancelerId; // 예약 취소자Id
    private String cancelerUserType; // 예약 취소자 userType


    private LocalDateTime startDt; // 산책시작 일시
    private LocalDateTime endDt; // 산책시작 일시

    private int diaryStatus; // 1 하나도 작성X  2 일부분  3 complete(모든 사진)
    private LocalDateTime diaryCreatedAt; // 산책일지 작성일시
    private LocalDateTime diaryUpdatedAt; // 산책일지 수정일시
    private int temperture; // 해당 산책에서 매긴 고객의 매너온도

    private List<CusRequireDto> cusRequireDtos;
    private List<BasicRequireDto> basicRequireDtos;

}
