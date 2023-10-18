package com.loveyourdog.brokingservice.model.entity;

import com.loveyourdog.brokingservice.model.dto.responseDto.InquiryResponseDto;
import com.loveyourdog.brokingservice.model.dto.responseDto.OfferResponseDto;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicUpdate
public class Inquiry  extends BaseTime{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 해당 엔티티 한정 identity 설정
    private Long id;


    // inquiry(N) : dogwalker(1)
    @JoinColumn(name="dogwalker")
    @ManyToOne(fetch = FetchType.LAZY)
    private Dogwalker dogwalker;

    public void setDogwalker(Dogwalker dogwalker) {
        this.dogwalker = dogwalker;
    }

    // inquiry(N) : customer(1)
    @JoinColumn(name="customer")
    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    // inquiry(N) : application(1)
    @JoinColumn(name="application")
    @ManyToOne(fetch = FetchType.LAZY)
    private Application application;
    public void setApplication(Application application) {
        this.application = application;
    }

    // inquiry(N) : commision(1)
    @JoinColumn(name="commision")
    @ManyToOne(fetch = FetchType.LAZY)
    private Commision commision;
    public void setCommision(Commision commision) {
        this.commision = commision;
    }

    // inquiry(1) : reservation(1)
    @OneToOne(mappedBy = "inquiry")
    private Reservation reservation; // 이 필드는 읽기 전용






    @ColumnDefault("0")
    private int price; // 문의 금액

    @ColumnDefault("0")
    private int status; // 상태
    // 0 : 취소된 문의
    // 1 : 단순 문의 (단순문의란? = 아직 수락,거절을 못받은 상태)
    // 2 : 수락을 받은 문의
    // 3 : 거절된 문의
    // 4 : 도그워커의 제안을 (내가)변경한 수락인 경우
    // 5 : 도그워커가(상대가) 변경하여 수락한 경우

    private LocalDateTime invalidatedAt; // 무효화 된 시점
    //  1. 도그워커로부터 거절된 경우(status 3)
    //  2. 고객이 직접 취소한 경우(status 0)
    //  3. 도그워커가(상대가) 변경하여 수락한 경우
    // 5.도그워커가(상대가) 변경하여 수락한 경우


    // created_at : 문의가 생성된 시점
    // = 고객이 문의를 한 시점(단순 문의)

    // updated_at : 문의가 수정된 시점



    public InquiryResponseDto toInquiryResponseDto(){
        InquiryResponseDto dto = InquiryResponseDto.builder()
                .price(price)
                .applicationId(application.getId())
                .commisionId(commision.getId())
                .dogwalkerId(dogwalker.getId())
                .customerId(customer.getId())
                .nick(customer.getNick())
                .gen(customer.getGen())
                .birthYear(customer.getBirthYear())
                .breed(commision.getBreed())
                .dogType(commision.getDogType())
                .dogAggr(commision.getDogAggr())
                .dogHealth(commision.getDogHealth())
                .startHour(commision.getStartHour())
                .startMin(commision.getStartMin())
                .endHour(commision.getEndHour())
                .endMin(commision.getEndMin())
                .inquiryCreatedAt(getCreatedDate())
                .inquiryId(getId())

                .cusDirName(customer.getDirName())
                .cusFileName(customer.getFileName())
                .cusExtension(customer.getExtension())
                .dwDirName(dogwalker.getDirName())
                .dwFileName(dogwalker.getFileName())
                .dwExtension(dogwalker.getExtension())

                .addrState(customer.getAddrState())
                .addrTown(customer.getAddrTown())
                .cusNick(customer.getNick())
                .dwNick(dogwalker.getNick())
                .month(commision.getMonth())
                .day(commision.getDay())
                .build();
        if(invalidatedAt!=null){
            dto.setInvalidatedAt(invalidatedAt);
        }
        return dto;
    }

}
