package com.loveyourdog.brokingservice.model.dto.requestDto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageRequestDto {

    private Long id; // 각 종 id
    private String dirName; // dwProfile cusProfile diary review main apply
    private String nick;
    private String profileMessage;
}
