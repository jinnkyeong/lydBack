package com.loveyourdog.brokingservice.model.entity;

import com.loveyourdog.brokingservice.model.enums.AccountType;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicUpdate
public class Customer extends BaseTime{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 해당 엔티티 한정 identity 설정
    private Long id;
    private Long socialId; // 소셜아이디=액세스토큰

    @Enumerated(EnumType.STRING)
    private AccountType accountType; // 계정 유형

    @Column(unique = true)
    private String email;

    private String pwd;

    private String name;
    @ColumnDefault("0")
    private int goalCnt;

    private int birthYear;

    private String gen; // m or f

    private String phone;

    private String nick;

    private String addrState;
    private String addrTown;
    private String addrDetail;

    @ColumnDefault("0")
    private int temperture;

    private String dirName; // S3 객체 이름

    private String fileName; // 이미지 파일 이름

    private String extension; // 파일 확장자

    @Lob
    private String profileMessage;
    @ColumnDefault("C")
    private String grade;


    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    private List<Authority> roles = new ArrayList<>();

    public void setRoles(List<Authority> roles) {
        this.roles = roles;
        roles.forEach(o -> o.setCustomer(this));
    }

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private List<Comment> comments;

//
//
//    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
//    @Builder.Default
//    private List<Certificate> certificates  = new ArrayList<>(); // 원하는 자격증
//    public void setCertificates(List<Certificate> certificates){
//        this.certificates = certificates;
//        certificates.forEach(o -> o.setCustomer(this));
//    }
    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Commision> commisions = new ArrayList<>();

    public void setCommisions(List<Commision> commisions) {
        this.commisions = commisions;
        commisions.forEach(o -> o.setCustomer(this));
    }

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Inquiry> inquiries = new ArrayList<>();

    public void setInquiries(List<Inquiry> inquiries) {
        this.inquiries = inquiries;
        inquiries.forEach(o -> o.setCustomer(this));
    }

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Offer> offers = new ArrayList<>();

    public void setOffers(List<Offer> offers) {
        this.offers = offers;
        offers.forEach(o -> o.setCustomer(this));
    }


    @OneToMany(mappedBy = "customer",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    private List<Alarm> alarms;

    @OneToMany(mappedBy = "customer",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    private List<Payment> payment;

    @ElementCollection // 선호하는 자격증
    private List<String> wishCertificates = new ArrayList<>();
}
