package com.loveyourdog.brokingservice.model.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class A  extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 해당 엔티티 한정 identity 설정
    private Long id;
    private String aName;

//    // 1. A(다) - B(일) 단방향
//    @ManyToOne
//    @JoinColumn(name="b_id") // A = 연관관계의 주인 = A가 B 변경,삭제 등 가능
//    private B b;


    // 2. A(다) - B(일) 양방향
    @ManyToOne
    @JoinColumn(name="b_id") // A = 연관관계의 주인 = A가 B 변경,삭제 등 가능
    private B b;
}
