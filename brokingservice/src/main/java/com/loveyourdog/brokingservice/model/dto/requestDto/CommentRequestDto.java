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
public class CommentRequestDto {


    private Long dogwalkerId;
    private Long customerId;

    private Long parentId;
    private String context;
    private Long reviewId;

}
