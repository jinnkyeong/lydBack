package com.loveyourdog.brokingservice.model.entity;

import com.loveyourdog.brokingservice.model.dto.responseDto.ReservationResponseDto;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicUpdate
public class Reservation  extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 해당 엔티티 한정 identity 설정
    private Long id;


    // reservation(1) : inquiry(1)
    @JoinColumn(name="inquiry")
    @OneToOne(fetch = FetchType.LAZY)
    private Inquiry inquiry;

    // reservation(1) : offer(1)
    @JoinColumn(name="offer")
    @OneToOne(fetch = FetchType.LAZY)
    private Offer offer;

    // reservation(1) : basicRequire(N)
    @OneToMany(mappedBy = "reservation", fetch = FetchType.LAZY)
    @Builder.Default
    private List<BasicRequire> basicRequires  = new ArrayList<>(); // 본사의 기본적인 수행사항
    public void addBasicRequires(List<BasicRequire> basicRequires){
        this.basicRequires.addAll(basicRequires);
        basicRequires.forEach(o -> o.setReservation(this));
    }

//     reservation(1) : cusRequire(N)
    @OneToMany(mappedBy = "reservation", fetch = FetchType.LAZY)
    @Builder.Default
    private List<CusRequire> cusRequires = new ArrayList<>(); // 고객의 요청된 수행사항
    public void addCusRequires(List<CusRequire> cusRequire){
        this.cusRequires.addAll(cusRequire);
        cusRequire.forEach(o -> o.setReservation(this));
    }



    // reservation(1) : review(1)
    @OneToOne(mappedBy = "reservation")
    private Review review; // 읽기전용

    @OneToOne(mappedBy = "reservation")
    private Payment payment;









    @ColumnDefault("1")
    private int status;
    // 0 : 취소된 예약
    // 1 : 예약 완료, 결제 전
    // 2 : 결제 완료, 산책 전
    // 3 : 산책 중
    // 4 : 산책 완료, 정산 전
    // 5 : 정산 중
    // 6 : 정산 완료

    private LocalDateTime canceledAt; // 예약 취소 일시
    private Long cancelerId; // 예약 취소자Id
    private String cancelerUserType; // 예약 취소자 userType


    private LocalDateTime startDt; // 산책시작 일시
    private LocalDateTime endDt; // 산책시작 일시

    @ColumnDefault("1")
    private int diaryStatus; // 1 하나도 작성X  2 일부분  3 complete(모든 사진)
    private LocalDateTime diaryCreatedAt; // 산책일지 작성일시
    private LocalDateTime diaryUpdatedAt; // 산책일지 수정일시
    @ColumnDefault("0")
    private int temperture; // 해당 산책에서 매긴 고객의 매너온도


    public ReservationResponseDto toResponseDto(String keyword){
        ReservationResponseDto responseDto = new ReservationResponseDto();

        if(keyword.equalsIgnoreCase("inquiry")){
            Application application = this.getInquiry().getApplication();
            Commision commision = this.getInquiry().getCommision();
            responseDto.setApplicationDto(application.toApplicationDto(application));
            responseDto.setCommisionDto(commision.toCommisionDto(commision));
            responseDto.setReservationId(this.getId());
            responseDto.setStatus(this.getStatus());
            responseDto.setReservCreatedAt(this.getCreatedDate());
            responseDto.setPrice(this.getInquiry().getPrice());
            responseDto.setCanceledAt(this.canceledAt);

            responseDto.setStartDt(this.getStartDt());
            responseDto.setEndDt(this.getEndDt());
            responseDto.setDiaryCreatedAt(this.diaryCreatedAt);
            responseDto.setDiaryUpdatedAt(this.diaryUpdatedAt);
            responseDto.setDiaryStatus(this.diaryStatus);

            responseDto.setCusDirName(commision.getCustomer().getDirName());
            responseDto.setCusFileName(commision.getCustomer().getFileName());
            responseDto.setCusExtension(commision.getCustomer().getExtension());
            responseDto.setDwDirName(application.getDogwalker().getDirName());
            responseDto.setDwFileName(application.getDogwalker().getFileName());
            responseDto.setDwExtension(application.getDogwalker().getExtension());


        } else if(keyword.equalsIgnoreCase("offer")){
            Application application = getOffer().getApplication();
            Commision commision = getOffer().getCommision();
            responseDto.setApplicationDto(application.toApplicationDto(application));
            responseDto.setCommisionDto(commision.toCommisionDto(commision));
            responseDto.setReservationId(this.getId());
            responseDto.setStatus(this.getStatus());
            responseDto.setReservCreatedAt(this.getCreatedDate());
            responseDto.setPrice(this.getOffer().getPrice());
            responseDto.setCanceledAt(this.canceledAt);

            responseDto.setStartDt(this.getStartDt());
            responseDto.setEndDt(this.getEndDt());
            responseDto.setDiaryCreatedAt(this.diaryCreatedAt);
            responseDto.setDiaryUpdatedAt(this.diaryUpdatedAt);
            responseDto.setDiaryStatus(this.diaryStatus);

            responseDto.setCusDirName(commision.getCustomer().getDirName());
            responseDto.setCusFileName(commision.getCustomer().getFileName());
            responseDto.setCusExtension(commision.getCustomer().getExtension());
            responseDto.setDwDirName(application.getDogwalker().getDirName());
            responseDto.setDwFileName(application.getDogwalker().getFileName());
            responseDto.setDwExtension(application.getDogwalker().getExtension());


        } else {
            System.out.println("keyword는 inquiry or offer 중 하나이어야 한다");
        }
        return responseDto;

    }






}
