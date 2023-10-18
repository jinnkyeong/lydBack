package com.loveyourdog.brokingservice.model.dto.responseDto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InquiryResponseDto {

    private Long applicationId;
    private Long commisionId;
    private Long dogwalkerId;
    private Long customerId;
    private Long inquiryId;

    private LocalDateTime inquiryCreatedAt;
    private int month; // 산책날짜
    private int day;
    private String addrState;
    private String addrTown;
    private String cusNick;
    private String dwNick;
    private String nick;
    private String gen;
    private int birthYear;
    private String breed;
    private int dogType;
    private int dogAggr;
    private int dogHealth;
    private int startHour;
    private int startMin;
    private int endHour;
    private int endMin;

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
}
