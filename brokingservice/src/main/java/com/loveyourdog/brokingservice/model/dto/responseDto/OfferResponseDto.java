package com.loveyourdog.brokingservice.model.dto.responseDto;

import com.loveyourdog.brokingservice.model.dto.querydsl.CertificateDto;
import com.loveyourdog.brokingservice.model.entity.Application;
import com.loveyourdog.brokingservice.model.entity.Commision;
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
public class OfferResponseDto {


    private Long applicationId;
    private Long commisionId;
    private Long dogwalkerId;
    private Long customerId;
    private Long inquiryId;
    private Long offerId;

    private LocalDateTime offerCreatedAt;
    private int month; // 산책날짜
    private int day;
    private String addrState;
    private String addrTown;
    private String cusNick;
    private String dwNick;

    private String cusDirName;
    private String cusFileName;
    private String cusExtension;
    private String dwDirName;
    private String dwFileName;
    private String dwExtension;

    private int price; // 제안 금액(기본값0)
    private int status; // 상태(기본값0)
    // 0 : 취소된 제안
    // 1 : 단순 제안 (단순제안이란? = 아직 수락,거절을 못받은 상태)
    // 2 : 수락을 받은 제안
    // 3 : 거절된 제안
    // 4 : 고객의 문의를 변경한 수락인 경우
    private LocalDateTime invalidatedAt; // 무효화 된 시점
    //  고객으로부터 거절된 경우
    //  or 도그워커가 직접 취소한 경우
    //  or 고객이 변경하여 수락한 경우

    private List<CertificateDto> certificates;
    private int birthYear;
    private String sentence;
    private int view;
}
