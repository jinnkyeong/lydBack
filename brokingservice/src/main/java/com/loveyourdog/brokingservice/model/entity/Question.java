package com.loveyourdog.brokingservice.model.entity;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicUpdate
public class Question extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 해당 엔티티 한정 identity 설정
    private Long id;

    // question(1) : questionType(1)
    @JoinColumn(name="questionType")
    @OneToOne(fetch = FetchType.LAZY)
    private QuestionType questionType; // 질문 타입

    private String input; // 입력받은 값


    @JoinColumn(name="dogwaker")
    @ManyToOne(fetch = FetchType.LAZY)
    private Dogwalker dogwalker;

    public void setDogwalker(Dogwalker dogwalker) {
        this.dogwalker = dogwalker;
    }



}
