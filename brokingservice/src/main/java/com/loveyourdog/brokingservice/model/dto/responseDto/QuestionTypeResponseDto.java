package com.loveyourdog.brokingservice.model.dto.responseDto;

import com.loveyourdog.brokingservice.model.entity.Question;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.ElementCollection;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
public class QuestionTypeResponseDto {

    private Long questionTypeId;
    private int type; // 문항 객관식 1 주관식 2
    private int cate; // 카테고리 코드 :   1(도그워킹 기술), 2(반려동물 행동 심리), 3(기초 훈련방법), 4(반려견 산책 유의사항)
    private String questionSentence;
    private String rightAnswer; // 정답
    private Set<String> wrongAnswers;
    private Set<String> answers; // 정답포함 보기들
    private int point; // 문항배점



}
