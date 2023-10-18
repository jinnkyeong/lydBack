package com.loveyourdog.brokingservice.model.entity;

import com.loveyourdog.brokingservice.model.dto.querydsl.*;
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
public class Commision extends BaseTime{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 해당 엔티티 한정 identity 설정
    private Long id;

    // commision(N) : customer(1)
    @JoinColumn(name="customer")
    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;

    // commision(1) : cus-require(N)
//    @OneToMany(mappedBy = "commision", fetch = FetchType.LAZY)
//    @Builder.Default
//    private List<CusRequireR> cusRequireRS = new ArrayList<>(); // 고객 요청사항
//    public void addCusRequires(List<CusRequireR> cusRequireRS){
//        this.cusRequireRS.addAll(cusRequireRS);
//        cusRequireRS.forEach(o -> o.setCommision(this));
//    }
    @ElementCollection
    @Builder.Default
    private List<String> cusRequireStrings = new ArrayList<>();

    // commision(1) : inquiry(N)
    @OneToMany(mappedBy = "commision", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Inquiry> inquiries  = new ArrayList<>(); // 문의(from 고객)
    public void addInquiries(List<Inquiry> inquiries){
        this.inquiries.addAll(inquiries);
        inquiries.forEach(o -> o.setCommision(this));
    }

    // commision(1) : offer(N)
    @OneToMany(mappedBy = "commision", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Offer> offers  = new ArrayList<>(); // 문의제안(from 도그워커)
    public void addOffers(List<Offer> offers){
        this.offers.addAll(offers);
        offers.forEach(o -> o.setCommision(this));
    }



    private int price; // 희망이용금액
    private int month; // 산책일자 월
    private int day; // 산책일자 일
    private String weekday; // 산책일자 요일
    private int startHour; // 산책시작 시
    private int startMin; // 산책시작 분
    private int endHour; // 산책종료 시
    private int endMin; // 산책종료 분


    private String dogName;
    private int dogAge;
    private int dogWeight;
    private int dogType; // 1소형견 2중형견 3대형견 4초대형견
    private String breed; // 견종
    private int dogAggr; // 공격성 1~5
    private int dogHealth; // 건강상태 1~5
    private String road; // 지정산책로


    @ColumnDefault("0")
    private int view;





    // dto로 변환
    public static CommisionDto toCommisionDto(Commision commision){

        // CusRequire -> CusRequireDto
        List<CusRequireDto> cDtos = new ArrayList<>();
        if(commision.getCusRequireStrings()!=null){
//            List<CusRequire> requires = commision.getCusRequireRS();
//            for (CusRequire c : requires) {
//                CusRequireDto dto = CusRequireDto.builder()
//                        .id(c.getId())
//                        .context(c.getContext()).build();
//                cDtos.add(dto);
//            }
        }
        // Commision -> CommsionDto(CusRequireDto담아서)
        CommisionDto commisionDto = CommisionDto.builder()
                .commisionId(commision.getId())
                .name(commision.getCustomer().getName())
                .nick(commision.getCustomer().getNick())
                .gen(commision.getCustomer().getGen())
                .birthYear(commision.getCustomer().getBirthYear())
                .addrState(commision.getCustomer().getAddrState())
                .addrTown(commision.getCustomer().getAddrTown())
                .addrDetail(commision.getCustomer().getAddrDetail())
                .temperture(commision.getCustomer().getTemperture())
                .price(commision.getPrice())
                .phone(commision.getCustomer().getPhone())
                .email(commision.getCustomer().getEmail())
                .month(commision.getMonth())
                .day(commision.getDay())
                .startHour(commision.getStartHour())
                .startMin(commision.getStartMin())
                .endHour(commision.getEndHour())
                .endMin(commision.getEndMin())
                .dogName(commision.getDogName())
                .dogAge(commision.getDogAge())
                .dogWeight(commision.getDogWeight())
                .breed(commision.getBreed())
                .dogType(commision.getDogType())
                .dogAggr(commision.getDogAggr())
                .dogHealth(commision.getDogHealth())
                .road(commision.getRoad())
                .view(commision.getView())
                .cusRequires(cDtos)
                .dirName(commision.getCustomer().getDirName())
                .fileName(commision.getCustomer().getFileName())
                .extension(commision.getCustomer().getExtension())
                .build();

        return commisionDto;
    }

}
