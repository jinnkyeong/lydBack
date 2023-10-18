package com.loveyourdog.brokingservice.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.loveyourdog.brokingservice.model.dto.obj.Image;
import com.loveyourdog.brokingservice.model.dto.requestDto.ImageRequestDto;
import com.loveyourdog.brokingservice.model.dto.responseDto.ProfileResponseDto;
import com.loveyourdog.brokingservice.model.entity.Customer;
import com.loveyourdog.brokingservice.model.entity.Dogwalker;
import com.loveyourdog.brokingservice.model.entity.Reservation;
import com.loveyourdog.brokingservice.repository.customer.CustomerRespository;
import com.loveyourdog.brokingservice.repository.dogwalker.DogwalkerRepository;
import com.loveyourdog.brokingservice.repository.reservation.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class StorageService {

    @Autowired
    private AmazonS3 amazonS3;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;


    private final DogwalkerRepository dogwalkerRepository;
    private final CustomerRespository customerRespository;



    //이미지 업로드 관련   -----------------------------------------------------------------------

    // 파일의 확장자만 추출  (ex) beagle.jpg -> jpg
    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 형식의 파일(" + fileName + ") 입니다.");
        }
    }

    // 중복되지 않는 파일이름 생성-> ganadara
    private String createFileName(String fileName) {
        return UUID.randomUUID().toString();
//        return UUID.randomUUID().toString().concat("."+getFileExtension(fileName)); // => randomFileName.jpg
    }

    // S3에 이미지 파일 업로드
    public List<Image> uploadToS3(String dirName, List<MultipartFile> files){
        List<Image> images = new ArrayList<>();
        if(files!=null && files.size() > 0) {
            files.forEach(file -> {
                String randomFileName = createFileName(file.getOriginalFilename()); // ganadara
                String extension = getFileExtension(file.getOriginalFilename()); // jpg
                String newFileName = randomFileName.concat("." + extension); // ganadara.jpg // 새로운 파일 이름 생성(중복X, 확장자는 동일)

                String key = dirName + "/" + newFileName; // pack1/ganadara.jpg

                // metadata 설정
                ObjectMetadata objectMetadata = new ObjectMetadata();
                objectMetadata.setContentLength(file.getSize());
                objectMetadata.setContentType(file.getContentType());

                // Public Read 권한으로 S3에 업로드
                try (InputStream inputStream = file.getInputStream()) {
                    amazonS3.putObject(
                            new PutObjectRequest(bucket, key, inputStream, objectMetadata)
                                    .withCannedAcl(CannedAccessControlList.PublicRead));
                } catch (IOException e) {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 업로드에 실패했습니다.");
                }

                Image image = Image.builder()
                        .dirName(dirName)
                        .fileName(randomFileName)
                        .extension(extension)
                        .build();
                images.add(image); // return용
            });
        }
        return images;
    }

    // S3, DB에 업로드(확장가능성 고려)
    public Image uploadProfile(List<MultipartFile> files, ImageRequestDto requestDto){

        // S3에 파일 업로드 한 후
        List<Image> images = uploadToS3(requestDto.getDirName(), files);

        // DB에 dirname, filename, extension 저장
        if(requestDto.getDirName().equalsIgnoreCase("dwProfile")){
            Dogwalker dogwalker  = dogwalkerRepository.findById(requestDto.getId()).orElseThrow(
                    ()-> new NoSuchElementException("id에 해당하는 도그워커가 없습니다"));
            dogwalker.setNick(requestDto.getNick());
            dogwalker.setProfileMessage(requestDto.getProfileMessage());
            // 이미지도 있으면 저장
            if(images!=null && images.size()>0) {
                for (Image img : images) {
                    dogwalker.setDirName(img.getDirName());
                    dogwalker.setFileName(img.getFileName());
                    dogwalker.setExtension(img.getExtension());
                }
            }
            dogwalkerRepository.save(dogwalker);

            // 고객 프로필 (nick도 저장)
        } else if(requestDto.getDirName().equalsIgnoreCase("cusProfile")){
            Customer customer  = customerRespository.findById(requestDto.getId()).orElseThrow(
                    ()-> new NoSuchElementException("id에 해당하는 고객이 없습니다"));
            customer.setNick(requestDto.getNick());
            customer.setProfileMessage(requestDto.getProfileMessage());
            if(images!=null && images.size()>0) {
                for (Image img : images) {
                    customer.setDirName(img.getDirName());
                    customer.setFileName(img.getFileName());
                    customer.setExtension(img.getExtension());
                }
            }
            customerRespository.save(customer);


        } else {
            throw new NoSuchElementException("dirName이 잘못되었습니다");
        }


        if(images!=null && images.size()>0 && images.get(0)!=null){
            return images.get(0); // 어차피 사진 하나만 있어서...
        } else {
            return Image.builder().build();
        }
    }


//    // 이미지 삭제 관련 -----------------------------------------------------------------
//    public void deleteImg(String dirName, String fileName){
//        String key = dirName + "/" + fileName; // 객체 이름 추가
//        if (!amazonS3.doesObjectExist(bucket, key)) {
//            throw new AmazonS3Exception("Object '" + key + "' does not exist!");
//        }
//        amazonS3.deleteObject(bucket, key);
//    }
}
