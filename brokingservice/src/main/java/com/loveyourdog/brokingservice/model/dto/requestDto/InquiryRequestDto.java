package com.loveyourdog.brokingservice.model.dto.requestDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InquiryRequestDto {

    private Long applicationId;
    private Long commisionId;
//    private Long dogwalkerId;
//    private Long customerId;
    private int price; // 제안 금액(기본값0)
//    private int status; // 상태(기본값0)
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
