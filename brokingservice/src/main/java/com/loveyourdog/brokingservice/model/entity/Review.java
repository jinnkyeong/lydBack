package com.loveyourdog.brokingservice.model.entity;

import com.loveyourdog.brokingservice.model.dto.responseDto.ReviewResponseDto;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.Duration;
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
public class Review extends BaseTime{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 해당 엔티티 한정 identity 설정
    private Long id;
    private int star; // 별점(0~10)
    @Column(nullable = false)
    @Lob
    private String context; // 10자 이상
    private String dirName; // S3 객체 이름
    private String fileName; // 이미지 파일 이름
    private String extension; // 파일 확장자

    // review(1) : reservation(1)
    @JoinColumn(name="reservation")
    @OneToOne(fetch = FetchType.LAZY)
    private Reservation reservation;

    // review(1) : comment(N)
    @OneToMany(mappedBy = "review", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Comment> comments  = new ArrayList<>(); // 댓글
    public void addComments(List<Comment> comments){
        this.comments.addAll(comments);
        comments.forEach(o -> o.setReview(this));
    }

    public ReviewResponseDto toDto(){
        Dogwalker dogwalker;
        Customer customer;
        Commision commision;
        if(reservation.getOffer()!=null){
            dogwalker = reservation.getOffer().getDogwalker();
            customer = reservation.getOffer().getCustomer();
            commision = reservation.getOffer().getCommision();
        } else{
            dogwalker = reservation.getInquiry().getDogwalker();
            customer = reservation.getInquiry().getCustomer();
            commision = reservation.getInquiry().getCommision();
        }
        // 소요시간
        Long seconds = Duration.between(reservation.getStartDt(), reservation.getEndDt()).getSeconds();
        Long hour = seconds / 3600;
        Long min = seconds % 3600 / 60;

        ReviewResponseDto responseDto = ReviewResponseDto.builder()
                .dogwalkerId(dogwalker.getId())
                .reviewId(getId())
                .star(getStar())
                .context(getContext())
                .dirName(getDirName())
                .fileName(getFileName())
                .extension(getExtension())
                .customerId(customer.getId())
                .customerNick(customer.getNick())
                .breed(commision.getBreed())
                .dogType(commision.getDogType())
                .month(commision.getMonth())
                .day(commision.getDay())
                .hour(hour)
                .min(min)
                .commentCnt(getComments().size())
                .addrState(customer.getAddrState())
                .addrTown(customer.getAddrTown())
                .build();
        return responseDto;
    }


}
