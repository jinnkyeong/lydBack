package com.loveyourdog.brokingservice.model.dto.responseDto;

import lombok.*;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BasicRequireResponseDto {



    private Long basicRequireId;
    private String context; // keyword->context
    private String keyword; // keyword
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
