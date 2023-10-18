package com.loveyourdog.brokingservice.service;

import com.loveyourdog.brokingservice.model.dto.obj.Image;
import com.loveyourdog.brokingservice.model.dto.querydsl.ZipcodeDto;
import com.loveyourdog.brokingservice.model.dto.requestDto.ImageRequestDto;
import com.loveyourdog.brokingservice.model.dto.requestDto.ProfileRequestDto;
import com.loveyourdog.brokingservice.model.dto.requestDto.PwdRequestDto;
import com.loveyourdog.brokingservice.model.dto.responseDto.ProfileResponseDto;
import com.loveyourdog.brokingservice.model.entity.Customer;
import com.loveyourdog.brokingservice.model.entity.Dogwalker;
import com.loveyourdog.brokingservice.model.entity.Zipcode;
import com.loveyourdog.brokingservice.repository.common.ZipcodeRepository;
import com.loveyourdog.brokingservice.repository.customer.CustomerRespository;
import com.loveyourdog.brokingservice.repository.dogwalker.DogwalkerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
public class CommonService {

    private final DogwalkerRepository dogwalkerRepository;
    private final CustomerRespository customerRespository;
    private final ZipcodeRepository zipcodeRepository;
    private final PasswordEncoder encoder;
    private final StorageService storageService;

    // profile 업데이트
    public Image updateProfile(List<MultipartFile> files, ImageRequestDto requestDto){
        return storageService.uploadProfile(files, requestDto);
    }



    // 비밀번호 확인
    // parameter : PwdRequestDto(비밀번호)
    // return : Boolean(true조회결과확인/false확인실패)
    public boolean checkPwd(PwdRequestDto requestDto) throws Exception {
        if(requestDto.getUserType().equalsIgnoreCase("customer")){
            Customer customer = customerRespository.findById(requestDto.getUserId()).orElseThrow(
                    ()-> new NoSuchElementException("이 id에 해당하는 고객이 없습니다"));
//            if(customer.getPwd().equals(encoder.encode(requestDto.getPwd()))){
//                return true;
//            }
            if(encoder.matches(customer.getPwd(), requestDto.getPwd())){
                return true;
            }
            return false;


        } else if (requestDto.getUserType().equalsIgnoreCase("dogwalker")) {
            Dogwalker dogwalker = dogwalkerRepository.findById(requestDto.getUserId()).orElseThrow(
                    ()-> new NoSuchElementException("이 id에 해당하는 도그워커가 없습니다"));
            System.out.println(dogwalker.getPwd());
            System.out.println(encoder.encode(requestDto.getPwd()));
//            if(dogwalker.getPwd().equals(encoder.encode(requestDto.getPwd()))){
//                return true;
//            }
            if(encoder.matches(dogwalker.getPwd(), requestDto.getPwd())){
                return true;
            }
            return false;

        } else {
            throw new Exception("유저 타입이 이상합니다");
        }
    }
    
    // 비밀번호 변경
    // parameter : PwdRequestDto(비밀번호)
    // return : Boolean(true변경성공/false변경실패)
    public boolean modifyPwd(PwdRequestDto requestDto){
        boolean result = false;
        if(requestDto.getUserType().equalsIgnoreCase("customer")){
            Customer customer = customerRespository.findById(requestDto.getUserId()).orElseThrow(
                    ()-> new NoSuchElementException("id에 해당하는 고객이 없습니다")
            );
            String newPwd = encoder.encode(requestDto.getPwd());
            customer.setPwd(newPwd);
            customerRespository.save(customer);
            result = true;
            
        } else if (requestDto.getUserType().equalsIgnoreCase("dogwalker")) {
            Dogwalker dogwalker  = dogwalkerRepository.findById(requestDto.getUserId()).orElseThrow(
                    ()-> new NoSuchElementException("id에 해당하는 도그워커가 없습니다")
            );
            String newPwd = encoder.encode(requestDto.getPwd());
            dogwalker.setPwd(newPwd);
            dogwalkerRepository.save(dogwalker);
            result = true;
        } else {
            new Exception("유저 타입이 이상합니다");
        }
        return result;
    }

    public List<ZipcodeDto> getZipcode(){
        List<ZipcodeDto> zipcodeDtos = new ArrayList<>();

        List<Zipcode> zipcodes = zipcodeRepository.findAll(
                Sort.by(Sort.Direction.ASC, "id"));

        for (Zipcode zc:zipcodes) {
            ZipcodeDto zipcodeDto = ZipcodeDto.builder()
                    .id(zc.getId())
                    .sido(zc.getSido())
                    .sigungu(zc.getSigungu())
                    .build();
            zipcodeDtos.add(zipcodeDto);
        }
        return zipcodeDtos;
    }


}
