package com.loveyourdog.brokingservice.model.dto.responseDto;

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
public class DiaryResponseDto {

    private Long reservationId;
    private Long customerId;
    private Long dogwalkerId;
    private Long commisionId;
    private Long applicationId;
    private Long inquiryId;
    private Long offerId;

    private List<BasicRequireDto> basicRequireDtos;
    private List<CusRequireDto>  cusRequireDtos;

    private LocalDateTime startDt; // 산책시작 일시
    private LocalDateTime endDt; // 산책시작 일시

    private int diaryStatus; // 1 하나도 작성X  2 일부분  3 complete(모든 사진)

    private LocalDateTime diaryCreatedAt; // 산책일지 작성일시
    private LocalDateTime diaryUpdatedAt; // 산책일지 수정일시
    @ColumnDefault("0")
    private int temperture; // 해당 산책에서 매긴 고객의 매너온도




}
