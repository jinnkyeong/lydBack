package com.loveyourdog.brokingservice.model.dto.querydsl;

import lombok.*;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BasicRequireDto {



    private Long basicRequireId;
    private String context; // 내용(에러무서워서 남겨뒀으나 필요없을듯)
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
    private boolean isEssential; // 필수 여부
    private int typeEssential; // 필수사항의 유형
    // 1: 방문 시
    // 2: 산책 전
    // 3: 산책 중
    // 4: 산책 완료

    private String dirName; // S3 객체 이름

    private String fileName; // 이미지 파일 이름

    private String extension; // 파일 확장자

}
