package com.loveyourdog.brokingservice.model.entity;

import lombok.*;

import javax.persistence.*;

// 도그워커 활동가능요일
@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Weekday {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 해당 엔티티 한정 identity 설정
    private Long id;
    private String day; // 근무 가능 요일 (sun,mon,tue,wed,thu,fri,sat 중 하나)

    @JoinColumn(name="application")
    @ManyToOne(fetch = FetchType.LAZY)
    private Application application;

    public void setApplication(Application application) {
        this.application = application;
    }
}
