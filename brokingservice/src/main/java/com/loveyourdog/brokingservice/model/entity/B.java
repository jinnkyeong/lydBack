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
public class B extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 해당 엔티티 한정 identity 설정
    private Long id;
    private String bName;
    // 1. A(다) - B(일) 단방향
    // B에서는 A를 참조하지 않음

    // 2. A(다) - B(일) 양방향
    @OneToMany(mappedBy = "b") // 양방향 매핑. mappedBy = 연관관계의 주인을 지정(변수명)
    List<A> as = new ArrayList<>();

    public void setAs(List<A> as) {
        this.as = as;
        as.forEach(o -> o.setB(this));
    }
}
