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
public class Lecture extends BaseTime{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 해당 엔티티 한정 identity 설정
    private Long id;

    private LocalDateTime endAt; // 해당 강의를 완강한 일시

    @JoinColumn(name="dogwaker")
    @ManyToOne(fetch = FetchType.LAZY)
    private Dogwalker dogwalker;
    public void setDogwalker(Dogwalker dogwalker) {
        this.dogwalker = dogwalker;
    }

//    @JoinColumn(name="lectureType")
//    @OneToOne(fetch = FetchType.LAZY)
//    private LectureType lectureType ;// 수강한 강의목록

    @JoinColumn(name="lectureType")
    @ManyToOne(fetch = FetchType.LAZY)
    private LectureType lectureType;
    public void setLectureType(LectureType lectureType) {
        this.lectureType = lectureType;
    }


}
