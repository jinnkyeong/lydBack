package com.loveyourdog.brokingservice.service;


import com.loveyourdog.brokingservice.model.dto.obj.Image;
import com.loveyourdog.brokingservice.model.dto.requestDto.LectureRequestDto;
import com.loveyourdog.brokingservice.model.dto.requestDto.ReviewRequestDto;
import com.loveyourdog.brokingservice.model.dto.responseDto.LectureResponseDto;
import com.loveyourdog.brokingservice.model.dto.responseDto.ReviewResponseDto;
import com.loveyourdog.brokingservice.model.entity.*;
import com.loveyourdog.brokingservice.repository.dogwalker.DogwalkerRepository;
import com.loveyourdog.brokingservice.repository.lecture.LectureRepository;
import com.loveyourdog.brokingservice.repository.lecture.LectureTypeRepository;
import com.loveyourdog.brokingservice.repository.reservation.ReservationRepository;
import com.loveyourdog.brokingservice.repository.review.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


@Service
@Transactional
@RequiredArgsConstructor
public class LectureService {

    private final LectureRepository lectureRepository;
    private final DogwalkerRepository dogwalkerRepository;
    private final LectureTypeRepository lectureTypeRepository;




    public String convertfromTypeToName(int type){
        // 1 : 도그워킹 기술
        // 2: 반려동물 행동 심리
        // 3: 기초 훈련방법
        // 4: 반려견 산책 유의사항
        switch (type){
            case 1:
                return "도그워킹 기술";
            case 2:
                return "반려동물 행동 심리";
            case 3:
                return "기초 훈련방법";
            case 4:
                return "반려견 산책 유의사항";
            default:
                return null;
        }
    }


    public List<LectureResponseDto> getLectureTypesByDwId(Long dwId){
        List<LectureResponseDto> responseDtos = new ArrayList<>();

        List<LectureType> lectureTypes = lectureTypeRepository.findAll();

        for (LectureType lectureType : lectureTypes) {
            List<Lecture> lectures = lectureType.getLectures();
            LectureResponseDto dto = LectureResponseDto.builder()
                    .title(lectureType.getTitle())
                    .id(lectureType.getId())
                    .build();
            if(lectures!=null && lectures.size()>0){
                for (Lecture lecture:lectures) {
                dto.setLectureId(lecture.getId());

                    if(lecture.getDogwalker().getId()==dwId){
                        if(lecture.getEndAt()!=null){
                            dto.setDogwalkerId(lecture.getDogwalker().getId());
                            dto.setEndAt(lecture.getEndAt());
                        }
                    }
                }

            }
            responseDtos.add(dto);


        }
        return responseDtos;
    }

//    public List<LectureResponseDto> getLecturesByDwId(Long dwId){
//        List<LectureResponseDto> responseDtos = new ArrayList<>();
//
//        Dogwalker dogwalker = dogwalkerRepository.findById(dwId).orElseThrow(()->{
//            throw  new NoSuchElementException("id에 일치하는 도그워커가 없습니다");
//        });
//
//        if(dogwalker.getLectures() != null || dogwalker.getLectures().size() > 0){
//
//            for (Lecture lecture:dogwalker.getLectures()) {
//                LectureResponseDto responseDto = new LectureResponseDto();
//                responseDto.setId(lecture.getId());
//                responseDto.setDogwalkerId(dogwalker.getId());
//                if(lecture.getType()>0 && convertfromTypeToName(lecture.getType())!=null){
//                    responseDto.setType(lecture.getType());
//                    responseDto.setTypeName(convertfromTypeToName(lecture.getType()));
//                }
//                if(lecture.getEndAt()!=null){
//                    responseDto.setEndAt(lecture.getEndAt());
//                }
//                responseDtos.add(responseDto);
//            }
//        }
//        return responseDtos;
//    }

    public boolean postLecture(LectureRequestDto requestDto){

        Dogwalker dogwalker = dogwalkerRepository.findById(requestDto.getDogwalkerId()).orElseThrow(()->{
            throw new NoSuchElementException("해당하는 도그워커가 없스빈다");
        });
        // findbyid.orelsethrow 두번쓰면 jpa initializer is redundant 이러면서 builder,생성자 다 못쓰게됨.
        // 임시방편으로 .orElseGet(LectureType::new); 함
//        for (Lecture lecture : dogwalker.getLectures()) {
//            if(lecture.getLectureType().getId()==requestDto.getLectureTypeId()){
//                lectureType = lecture.getLectureType();
//            }
//
//        }
//        if(lectureType==null){
//            throw new NoSuchElementException("해당하는 강의타입이 없습니다");
//        }
        LectureType  lectureType = lectureTypeRepository.findById(requestDto.getLectureTypeId()).orElseGet(LectureType::new);


        Lecture lecture =  Lecture.builder()
                .lectureType(lectureType)
                .dogwalker(dogwalker)
                .build();
        if(requestDto.getEndAt()!=null){
            lecture.setEndAt(requestDto.getEndAt());
        }
        lectureRepository.save(lecture);
        return true;
    }

}
