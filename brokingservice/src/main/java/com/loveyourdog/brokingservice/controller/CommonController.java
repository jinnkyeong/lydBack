package com.loveyourdog.brokingservice.controller;

import com.loveyourdog.brokingservice.model.dto.obj.Image;
import com.loveyourdog.brokingservice.model.dto.querydsl.ZipcodeDto;
import com.loveyourdog.brokingservice.model.dto.requestDto.ImageRequestDto;
import com.loveyourdog.brokingservice.model.dto.requestDto.ProfileRequestDto;
import com.loveyourdog.brokingservice.model.dto.requestDto.PwdRequestDto;
import com.loveyourdog.brokingservice.model.dto.responseDto.ProfileResponseDto;
import com.loveyourdog.brokingservice.service.CommonService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Api(tags = "공통 컨트롤러")
@RestController
@RequiredArgsConstructor
@RequestMapping(value="/api")
public class CommonController {

    private final CommonService commonService;


    // 프로필 사진(S3,DB), 닉네임 변경
    @PostMapping(value = "/profile", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Image> updateProfile(
            @RequestPart(value="image", required=false) List<MultipartFile> files,
            @RequestPart(value = "requestDto") ImageRequestDto requestDto
    ){
        return new ResponseEntity<>(commonService.updateProfile(files,requestDto), HttpStatus.OK);
    }



    // 비밀번호 변경
    // 인증 목적 조회
    @PostMapping("/profile/pwd/check")
    public ResponseEntity<Boolean> checkPwd(@RequestBody PwdRequestDto requestDto) throws Exception {
        return new ResponseEntity<>(commonService.checkPwd(requestDto), HttpStatus.OK);
    }
    // 변경
    @PostMapping("/profile/pwd/modify")
    public ResponseEntity<Boolean> modifyPwd(@RequestBody PwdRequestDto requestDto){
        return new ResponseEntity<>(commonService.modifyPwd(requestDto), HttpStatus.OK);
    }


    // 시도 시군구
    @GetMapping("/zipcode")
    public ResponseEntity<List<ZipcodeDto>> getZipcode(){
        return new ResponseEntity<>(commonService.getZipcode(), HttpStatus.OK);
    }



}
