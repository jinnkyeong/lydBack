package com.loveyourdog.brokingservice.model.dto.requestDto;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

// 프로필사진,닉네임만 변경가능

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileRequestDto {

    private Long userId; // 유저 아이디
    private String userType; // 유저타입
    @ApiModelProperty(example="닉네임")
    private String nick;
    private List<MultipartFile> files;
//    @ApiModelProperty(example="profile")
//    private String dirName; // S3 객체 이름
//    @ApiModelProperty(example="pf1")
//    private String fileName; // 이미지 파일 이름
//    @ApiModelProperty(example="jpg")
//    private String extension; // 파일 확장자
}
