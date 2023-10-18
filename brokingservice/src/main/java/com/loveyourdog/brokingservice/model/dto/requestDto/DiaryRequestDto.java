package com.loveyourdog.brokingservice.model.dto.requestDto;

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
public class DiaryRequestDto {

    private Long reservationId;
    private List<String> keywords;
    private List<Long> cusRequireIds;


    private LocalDateTime diaryUpdatedAt;
    private LocalDateTime diaryCreatedAt;

    private int diaryStatus;
    private int temperture;

//    private List<BasicRequireRequestDto> basicRequireDtos; // img-br 사이 연결...
//    private List<CusRequireRequestDto> cusRequireDtos; // 마찬가지



}
