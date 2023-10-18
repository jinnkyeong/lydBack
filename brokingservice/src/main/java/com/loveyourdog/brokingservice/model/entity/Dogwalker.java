package com.loveyourdog.brokingservice.model.entity;

import com.loveyourdog.brokingservice.model.enums.AccountType;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.security.AuthProvider;
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
public class Dogwalker extends BaseTime{

//    private AuthProvider authProvider;

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

    @ColumnDefault("false")
    private boolean interviewPassed; // 인터뷰 통과 여부

    @ColumnDefault("false")
    private boolean appicationPassed; // 지원서 통과 여부

    @ColumnDefault("false")
    private boolean passed; // 최종합격여부

    private String depositBank; // 입금 은행

    private String depositAccount; // 입금 계좌번호(번호만)


    private String dirName; // S3 객체 이름

    private String fileName; // 이미지 파일 이름

    private String extension; // 파일 확장자
    
    private int star; // 평균 별점

    private int testScore; // 점수
    private LocalDateTime testStartAt; // 시작일시
    private LocalDateTime testEndAt; // 종료일시

    @Lob
    private String profileMessage;
    @ColumnDefault("C")
    private String grade;


    @OneToMany(mappedBy = "dogwalker",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @Builder.Default
    private List<Application> applications = new ArrayList<>();
    public void setApplications(List<Application> applications) {
        this.applications = applications;
        applications.forEach(o->o.setDogwalker(this));
    }
    public void addApplications(List<Application> applications) {
        this.applications.addAll(applications);
        applications.forEach(o->o.setDogwalker(this));
    }




    @OneToMany(mappedBy = "dogwalker",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @Builder.Default
    private List<Authority> roles = new ArrayList<>();
    public void setRoles(List<Authority> roles) {
        this.roles = roles;
        roles.forEach(o -> o.setDogwalker(this));
    }

    @OneToMany(mappedBy = "dogwalker",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval=true
    ) // cascade 하면 array remove unsupported 뜸;;
    @Builder.Default
    private List<Certificate> certificates = new ArrayList<>(); // 자격증
    public void setCertificates(List<Certificate> certificates){
        this.certificates = certificates; // Dogwalker의 certificates를 업데이트
        certificates.forEach(o -> o.setDogwalker(this)); // 위의 변경을 Certificate의 dogwalker에 다시 업데이트
    }
    public void addCertificates(List<Certificate> certificates){
        this.certificates.addAll(certificates); // Dogwalker의 certificates를 업데이트
        certificates.forEach(o -> o.setDogwalker(this)); // 위의 변경을 Certificate의 dogwalker에 다시 업데이트
        System.out.println("자격증! "+certificates.get(0));
    }

    @OneToMany(mappedBy = "dogwalker",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    private List<Lecture> lectures;// 수강한 강의목록



    @OneToMany(mappedBy = "dogwalker",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    private List<Question> questions;


    @OneToMany(mappedBy = "dogwalker",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    private List<Comment> comments;


    @OneToMany(mappedBy = "dogwalker",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    ) // cascade 하면 array remove unsupported 뜸;;
    @Builder.Default
    private List<Inquiry> inquiries  = new ArrayList<>(); // 자격증

    public void setInquiries(List<Inquiry> inquiries) {
        this.inquiries = inquiries;
        inquiries.forEach(o -> o.setDogwalker(this));
    }

    @OneToMany(mappedBy = "dogwalker",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    ) // cascade 하면 array remove unsupported 뜸;;
    @Builder.Default
    private List<Offer> offers  = new ArrayList<>(); // 자격증

    public void setOffers(List<Offer> offers) {
        this.offers = offers;
        offers.forEach(o -> o.setDogwalker(this));
    }

    @OneToMany(mappedBy = "dogwalker",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    private List<Alarm> alarms;


}
