package com.loveyourdog.brokingservice.model.dto.requestDto;

import lombok.*;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BasicRequireRequestDto {



    private String  keyword; // 키워드를 입력,저장
    // 다대다 피하기 위해서 context 없애고 하드코딩하려고 함....
    // cmc : 반려견과 커뮤니케이션 하기(최소 15분)
    // rop : 산책줄을 올바르게 착용하기
    // dor : 문 단속하기
    // rea : 준비물을 모두 챙겼는지 확인하기
    // rod  : 고객님이 지정하신 산책로에서 산책하기
    // pac : 반려견의 건강상태를 고려한 페이스 조절
    // rel : 최소 10분 간격으로 물을 마시게 하기
    // bak : 고객님 댁으로 강아지를 데려다 주기
    // rpb : 강아지가 다쳤는지 간단히 체크하기
    //




}
