package com.loveyourdog.brokingservice.model.entity;

import com.loveyourdog.brokingservice.model.dto.responseDto.BasicRequireResponseDto;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BasicRequire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String  keyword; // 키워드를 입력,저장
    // 다대다 피하기 위해서 context 없애고 하드코딩하려고 함....
    // cmc : 반려견과 커뮤니케이션 하기(최소 15분)
    // rop : 산책줄을 올바르게 착용하기
    // dor : 문 단속하기
    // rea : 준비물을 모두 챙겼는지 확인하기
    // rod  : 고객님이 지정하신 산책로에서 산책하기
    // pac : 반려견의 건강상태를 고려한 페이스 조절
    // rel : 최소 10분 간격으로 물을 마시게 하기
    // bak : 고객님 댁으로 강아지를 데려다 주기
    // rpb : 강아지가 다쳤는지 간단히 체크하기
    //

    private int typeEssential; // 필수사항의 유형
    // 1: 방문 시 : cmc, rop
    // 2: 산책 전 : dor, rea
    // 3: 산책 중 : rod, pac, rel
    // 4: 산책 완료 : bak, rpb

    private String dirName; // S3 객체 이름

    private String fileName; // 이미지 파일 이름

    private String extension; // 파일 확장자







    // basicRequire(N) : reservation(1)
    @JoinColumn(name="reservation")
    @ManyToOne(fetch = FetchType.LAZY)
    private Reservation reservation;
    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }


    // keyword -> context
    public String fromKeywordToContext(String keyword) {
        // cmc : 반려견과 커뮤니케이션 하기(최소 15분)
        // rop : 산책줄을 올바르게 착용하기
        // dor : 문 단속하기
        // rea : 준비물을 모두 챙겼는지 확인하기
        // rod  : 고객님이 지정하신 산책로에서 산책하기
        // pac : 반려견의 건강상태를 고려한 페이스 조절
        // rel : 최소 10분 간격으로 물을 마시게 하기
        // bak : 고객님 댁으로 강아지를 데려다 주기
        // rpb : 강아지가 다쳤는지 간단히 체크하기
        String context = null; // keyword->context
        switch (keyword){
            case "cmc" :
                context = "반려견과 커뮤니케이션 하기(최소 15분)";
                break;
            case "rop":
                context = "산책줄을 올바르게 착용하기";
                break;
            case "dor":
                context = "문 단속하기";
                break;
            case "rea":
                context = "준비물을 모두 챙겼는지 확인하기";
                break;
            case "rod":
                context = "고객님이 지정하신 산책로에서 산책하기";
                break;
            case "pac":
                context = "반려견의 건강상태를 고려한 페이스 조절";
                break;
            case "rel":
                context = "최소 10분 간격으로 물을 마시게 하기";
                break;
            case "bak":
                context = "고객님 댁으로 강아지를 데려다 주기";
                break;
            case "rpb":
                context = "강아지가 다쳤는지 간단히 체크하기";
                break;
            default:
                break;
        }
        return context;
    }

    public BasicRequireResponseDto toDto(){


        String context = fromKeywordToContext(getKeyword());
        BasicRequireResponseDto dto = new BasicRequireResponseDto();
        dto.setBasicRequireId(getId());
        dto.setContext(context);
        dto.setTypeEssential(getTypeEssential());
        dto.setDirName(getDirName());
        dto.setFileName(getFileName());
        dto.setExtension(getExtension());
        dto.setKeyword(getKeyword());
        return dto;
    }

}
