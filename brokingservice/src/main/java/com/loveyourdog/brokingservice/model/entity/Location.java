package com.loveyourdog.brokingservice.model.entity;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;


// 도그워커 활동가능지역(주소랑 별개)
@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicUpdate
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 해당 엔티티 한정 identity 설정
    private Long id;

    private String state; // 시도
    private String town; // 시군구



    @JoinColumn(name="application")
    @ManyToOne(fetch = FetchType.LAZY)
    private Application application;
    public void setApplication(Application application) {
        if(this.application!=null){
            this.application.getLocations().remove(this);
        }
        this.application = application;
        if(application != null){
            application.getLocations().add(this);
        }
    }
}
