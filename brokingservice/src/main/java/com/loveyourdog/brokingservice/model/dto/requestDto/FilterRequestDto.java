package com.loveyourdog.brokingservice.model.dto.requestDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FilterRequestDto {

    private int maxPrice; // 최대금액
    private int minPrice; // 최소금액
    // 추가해야 됨 자격증 등...

}
