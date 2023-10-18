package com.loveyourdog.brokingservice.model.entity;

import com.loveyourdog.brokingservice.model.dto.querydsl.CertificateDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


// 도그워커의 자격증
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Certificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String keyword; // 하드코딩..
    // dw: 도그워커
    // trn : 훈련사
    // act : 반려동물행동교정사
    // lea : 반려견지도사
    // fun : 반려동물장례지도사
    // bre : 애견브리더
    // lec : 펫시터강사
    // sit : 펫시터
    // mut : 동물매개활동관리사
    // sty : 애견미용사
    // foo : 반려동물식품관리사

    @JoinColumn(name="dogwalker")
    @ManyToOne(fetch = FetchType.LAZY)
    private Dogwalker dogwalker;
    public void setDogwalker(Dogwalker dogwalker) {
        this.dogwalker = dogwalker;
    }

    public CertificateDto toDto(){
        CertificateDto dto = CertificateDto.builder()
                .id(id)
                .keyword(keyword)
                .build();
        return dto;
    }

}