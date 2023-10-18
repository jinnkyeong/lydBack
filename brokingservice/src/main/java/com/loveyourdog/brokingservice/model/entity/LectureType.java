package com.loveyourdog.brokingservice.model.entity;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicUpdate
public class LectureType extends BaseTime{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 해당 엔티티 한정 identity 설정
    private Long id;
//    private int type;
//    // 1 : 도그워킹 기술
//    // 2: 반려동물 행동 심리
//    // 3: 기초 훈련방법
//    // 4: 반려견 산책 유의사항
    private String title;


    @OneToMany(mappedBy = "lectureType",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    private List<Lecture> lectures ;


}
