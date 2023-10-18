package com.loveyourdog.brokingservice.model.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicUpdate
public class QuestionType extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 해당 엔티티 한정 identity 설정
    private Long id;
    @ColumnDefault("1")
    private int type; // 문항 객관식 1 주관식 2
    @ColumnDefault("1")
    private int cate; // 카테고리 코드 :   1(도그워킹 기술), 2(반려동물 행동 심리), 3(기초 훈련방법), 4(반려견 산책 유의사항)
    private String questionSentence;
    private String rightAnswer; // 정답
    @ElementCollection // 오답들(객관식)
    @Builder.Default
    private Set<String> wrongAnswers = new HashSet<>();
    @ColumnDefault("0")
    private int point; // 문항배점

    @OneToOne(mappedBy = "questionType")
    private Question question;




}
