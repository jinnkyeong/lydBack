package com.loveyourdog.brokingservice.model.entity;

import com.loveyourdog.brokingservice.model.dto.querydsl.CertificateDto;
import com.loveyourdog.brokingservice.model.dto.responseDto.OfferResponseDto;
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
public class Offer  extends BaseTime{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 해당 엔티티 한정 identity 설정
    private Long id;

    // offer(N) : dogwalker(1)
    @JoinColumn(name="dogwalker")
    @ManyToOne(fetch = FetchType.LAZY)
    private Dogwalker dogwalker;

    public void setDogwalker(Dogwalker dogwalker) {
        this.dogwalker = dogwalker;
    }


    // offer(N) : customer(1)
    @JoinColumn(name="customer")
    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }





    // offer(N) : application(1)
    @JoinColumn(name="application")
    @ManyToOne(fetch = FetchType.LAZY)
    private Application application;
    public void setApplication(Application application) {
        this.application = application;
    }

    // offer(N) : commision(1)
    @JoinColumn(name="commision")
    @ManyToOne(fetch = FetchType.LAZY)
    private Commision commision;
    public void setCommision(Commision commision) {
        this.commision = commision;
    }

    // offer(1) : reservation(1)
    @OneToOne(mappedBy = "offer")
    private Reservation reservation; // 이 필드는 읽기 전용


    @ColumnDefault("0")
    private int price; // 제안 금액

    @ColumnDefault("0")
    private int status; // 상태
    // 0 : 취소된 제안
    // 1 : 단순 제안 (단순제안이란? = 아직 수락,거절을 못받은 상태)
    // 2 : 수락을 받은 제안
    // 3 : 거절된 제안
    // 4 : 고객의 문의를 변경한 수락인 경우

    private LocalDateTime invalidatedAt; // 무효화 된 시점
    //  1. 고객으로부터 거절된 경우
    //  2. 도그워커가 직접 취소한 경우
    //  3. 고객이 변경하여 수락한 경우

    // created_at : 제안이 생성된 시점 = 도그워커가 제안를 한 시점(단순 제안)
    // updated_at : 제안이 수정된 시점


    public OfferResponseDto toOfferResponseDto(){
        System.out.println("price : "+price);
        System.out.println("application : "+application.getId());
        System.out.println("commision : "+commision.getId());
        List<CertificateDto> certificateDtos = new ArrayList<>();
        for (Certificate cert:dogwalker.getCertificates()) {
            certificateDtos.add(cert.toDto());
        }
        OfferResponseDto dto = OfferResponseDto.builder()
                .offerCreatedAt(getCreatedDate())
                .dogwalkerId(application.getDogwalker().getId())
                .customerId(commision.getCustomer().getId())
                .cusNick(commision.getCustomer().getNick())
                .dwNick(application.getDogwalker().getNick())
                .sentence(application.getSentence())
                .birthYear(dogwalker.getBirthYear())
                .certificates(certificateDtos)
                .cusDirName(customer.getDirName())
                .cusFileName(customer.getFileName())
                .cusExtension(customer.getExtension())
                .dwDirName(dogwalker.getDirName())
                .dwFileName(dogwalker.getFileName())
                .dwExtension(dogwalker.getExtension())

                .month(commision.getMonth())
                .day(commision.getDay())
                .addrState(commision.getCustomer().getAddrState())
                .addrTown(commision.getCustomer().getAddrTown())
                .status(status)
                .invalidatedAt(invalidatedAt)
                .price(price)
                .view(application.getView())
                .applicationId(application.getId())
                .commisionId(commision.getId())
                .offerId(id)
                .build();
        return dto;
    }


}
