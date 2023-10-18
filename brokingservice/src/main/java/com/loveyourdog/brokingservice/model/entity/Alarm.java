package com.loveyourdog.brokingservice.model.entity;

import com.loveyourdog.brokingservice.model.enums.AccountType;
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
public class Alarm extends BaseTime{



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 해당 엔티티 한정 identity 설정
    private Long id;


    @JoinColumn(name="dogwalker")
    @ManyToOne(fetch = FetchType.LAZY)
    private Dogwalker dogwalker;
    public void setDogwalker(Dogwalker dogwalker) {
        this.dogwalker = dogwalker;
    }

    @JoinColumn(name="customer")
    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }



    private int msgType;
    // 1 : 청약자가 지원서or의뢰서 변경 -> 미수락자
    // 2 : 예약 완료 -> 청약자
    // 3 : 합격 결정 완료 -> 합격자
    // 4 : 대댓글 작성 -> 부모댓글러  2->1   3->2   4->3 ...
    // 5 : 댓글,대댓글 작성 -> 원글러 1,2,3,4 -> 0

    @ColumnDefault("1")
    private int checked; // 1 : 미확인 2 : 확인

    private Long paperId; // 지원서나 의뢰서가 변경된 경우 - 변경된 서류의 id
//    private Long reservId; // 예약이 완료 경우 - 예약 id
//
//    private Long reviewId; // 원글인 리뷰 id
//    private Long commentId; // 작성된 댓글 id
    private Long parentCommentId; // 작성된 댓글의 부모 댓글

    @JoinColumn(name="reservation")
    @ManyToOne(fetch = FetchType.LAZY)
    private Reservation reservation;

    @JoinColumn(name="review")
    @ManyToOne(fetch = FetchType.LAZY)
    private Review review;

    @JoinColumn(name="comment")
    @ManyToOne(fetch = FetchType.LAZY)
    private Comment comment;
//
//
//
//    @ColumnDefault("0")
//    private int depth; // 깊이





}
