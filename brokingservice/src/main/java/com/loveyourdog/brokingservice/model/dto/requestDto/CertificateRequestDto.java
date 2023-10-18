package com.loveyourdog.brokingservice.model.dto.requestDto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CertificateRequestDto {


    @ApiModelProperty(example="훈련사")
    private String name;
    @ApiModelProperty(example="훈련사자격증입니다")
//    private String info;
//    @ApiModelProperty(example="1")
    private Long dogwalkerId;

}
