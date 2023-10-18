package com.loveyourdog.brokingservice.model.entity;


// 고객의 요청사항 - 예약
// 예약 생성시 commision의 cusrequire string list가 여기로 세팅
// 이미지 유지

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.loveyourdog.brokingservice.model.dto.querydsl.CusRequireDto;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CusRequire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String context; // 내용

    private String dirName; // S3 객체 이름

    private String fileName; // 이미지 파일 이름

    private String extension; // 파일 확장자

    private Long commisionId; // 관계매핑 없이 일단 ....


    // cus-require(N) : commision(1)
//    @JoinColumn(name="commision")
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JsonIgnore
//    private Commision commision;
//    public void setCommision(Commision commision) {
//        this.commision = commision;
//    }


    // cus-require(N) : reservation(1)
    @JoinColumn(name="reservation")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Reservation reservation;
    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }


    public CusRequireDto toCusRequireDto(){

        CusRequireDto dto = new CusRequireDto();
        dto.setId(id);
        dto.setContext(context);
        dto.setDirName(dirName);
        dto.setFileName(fileName);
        dto.setExtension(extension);
        dto.setCommisionId(commisionId);
        return dto;
    }
}

