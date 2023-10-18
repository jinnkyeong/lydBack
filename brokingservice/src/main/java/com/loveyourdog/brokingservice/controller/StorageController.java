package com.loveyourdog.brokingservice.controller;

import com.loveyourdog.brokingservice.model.dto.requestDto.ImageRequestDto;
import com.loveyourdog.brokingservice.service.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value="/api")
public class StorageController {
    @Autowired
    private StorageService storageService;



    // 프론트엔드에 directory + filename를 전달  (ex) /test1/beagle.jpg
    // 한 개의 경우
//    @GetMapping("/test2/{imgNo}")
//    public String getOnePath(@PathVariable Long imgNo) {
//        Image image = imageService.findByImgNo(imgNo);
//        //System.out.println(image.getImgNo());
////        String dirName = image.getDirName();
////        String fileName = image.getFileName();
////        String path = "/"+dirName + "/" + fileName;
//
//        //return path;
//        return "dd";
//    }
//
//    // 여러 개의 경우
//    @GetMapping("/test2")
//    public List<Picture> getAllPath() {
//        List<Picture> list = new ArrayList<>();
//
//        List<Image> images = imageService.findAllImage();
//        for (Image image : images) {
//            //System.out.println(image.getImgNo());
//
//            String dirName = image.getDirName();
//            String fileName = image.getFileName();
//            Long no = image.getImgNo();
//
//            Picture picture = new Picture();
//            picture.setDirName(dirName);
//            picture.setFileName(fileName);
//            picture.setId(no); // 이미지번호
//            picture.setActive(false);
//            picture.setDelete(false);
//            list.add(picture);
//        }
//        return list;
//    }
//

    // 클라이언트로부터 이미지 파일(multifile 허용) 받아서 업로드, 새 파일명으로 저장
//    @PostMapping(value = "/uploadImg", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
//    public ResponseEntity<String> upload(
//            @RequestPart(value="image", required=false) List<MultipartFile> files,
//            @RequestPart(value = "requestDto") ImageRequestDto requestDto
//    ){
//        return new ResponseEntity<>(storageService.uploadImg(files, requestDto), HttpStatus.OK);
//    }
//
//    // 클라이언트가 선택한 이미지파일 삭제
//    @PostMapping("/test4")
//    public String remove(@RequestBody Picture picture){
//        log.info("picture : {}",picture);
//
//        // S3에 이미지 삭제 요청
//        s3service.deleteImg(picture.getDirName(), picture.getFileName());
//
//        // DB에서 삭제
//        imageService.deleteById(picture.getId());
//
//        return "삭제 완료";
//    }


}
