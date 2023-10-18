package com.loveyourdog.brokingservice.service;


import com.loveyourdog.brokingservice.model.dto.requestDto.QuestionRequestDto;
import com.loveyourdog.brokingservice.model.dto.responseDto.LectureResponseDto;
import com.loveyourdog.brokingservice.model.dto.responseDto.QuestionTypeResponseDto;
import com.loveyourdog.brokingservice.model.entity.Dogwalker;
import com.loveyourdog.brokingservice.model.entity.Lecture;
import com.loveyourdog.brokingservice.model.entity.Question;
import com.loveyourdog.brokingservice.model.entity.QuestionType;
import com.loveyourdog.brokingservice.repository.dogwalker.DogwalkerRepository;
import com.loveyourdog.brokingservice.repository.lecture.LectureRepository;
import com.loveyourdog.brokingservice.repository.test.QuestionRepository;
import com.loveyourdog.brokingservice.repository.test.QuestionTypeRepository;
import com.querydsl.core.types.ParamNotSetException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Service
@Transactional
@RequiredArgsConstructor
public class TestService {

    private final QuestionRepository questionRepository;
    private final QuestionTypeRepository questionTypeRepository;
    private final DogwalkerRepository dogwalkerRepository;




    // 테스트 문제(전부 출력)
    // () -> question type list
    public List<QuestionTypeResponseDto> getQuestionTypes(){
        List<QuestionTypeResponseDto> responseDtos = new ArrayList<>();

        List<QuestionType> questionTypes =  questionTypeRepository.findAll();
            for (QuestionType type: questionTypes) {
                Set<String> wrongAnswers = new HashSet<>(); // 오답들
                Set<String> answers = new HashSet<>(); // 정답+오답들
                QuestionTypeResponseDto dto = QuestionTypeResponseDto.builder()
                        .questionTypeId(type.getId())
                        .type(type.getType())
                        .cate(type.getCate())
                        .questionSentence(type.getQuestionSentence())
                        .rightAnswer(type.getRightAnswer())
                        .point(type.getPoint())
                        .build();
                answers.add(type.getRightAnswer());
                for (String a:type.getWrongAnswers()) {
                    wrongAnswers.add(a);
                    answers.add(a);
                }
                dto.setAnswers(answers); // 첫번째가 정답. 나머지는 오답
                dto.setWrongAnswers(wrongAnswers);

                responseDtos.add(dto);
            }

        return responseDtos;
    }




    public boolean createTest(Long dwId, String testStartAt, String testEndAt,
                              List<QuestionRequestDto> requestDtos) throws Exception {

        questionRepository.deleteAll();

        int sum = 0;
        List<Question> questions = new ArrayList<>();

        if(dwId!=null && testStartAt!=null && testEndAt!=null){

            Dogwalker dogwalker = dogwalkerRepository.findById(dwId).orElseThrow(()->{
                   throw new NoSuchElementException("id에 해당하는 도그워커가 없습니다");
            });
            // 시작 및 종료시간 저장
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); //2021-05-30 15:47:29 -> localdatetime
            LocalDateTime startDt = LocalDateTime.parse(testStartAt, formatter);
            LocalDateTime endDt = LocalDateTime.parse(testEndAt, formatter);
            dogwalker.setTestStartAt(startDt); // 시험시작시간
            dogwalker.setTestEndAt(endDt); // 시험종료시간

            // Question 기록 생성
            for (QuestionRequestDto reqDto: requestDtos) {
                if(reqDto.getQuestionTypeId()!=null && reqDto.getInput()!=null){
                    QuestionType questionType = questionTypeRepository.findById(reqDto.getQuestionTypeId()).orElseThrow(()->{
                        throw new NoSuchElementException("id에 해당하는 질문 타입이 없습니다");
                    });
                    Question question = Question.builder()
                            .dogwalker(dogwalker)
                            .questionType(questionType)
                            .input(reqDto.getInput())
                            .build();
                    questions.add(question);

                    if(reqDto.getInput().equalsIgnoreCase(questionType.getRightAnswer())){
                        sum += questionType.getPoint();
                    }
                }
            }
            // 테스트 점수 저장
            dogwalker.setTestScore(sum);
            dogwalkerRepository.save(dogwalker);

            questionRepository.saveAll(questions);

            // 테스트 합격 여부 리턴
            if(sum >= 80){
                return true;
            } else {
                return false;
            }
        } else {
            throw new Exception("dwId,testStartAt,testEndAt를 모두 주세요");
        }
    }






}
