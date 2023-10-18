package com.loveyourdog.brokingservice.model.entity;

import com.loveyourdog.brokingservice.model.dto.responseDto.ReservationResponseDto;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "zipcode")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Zipcode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 해당 엔티티 한정 identity 설정
    private Long id;

    private String sido;
    private String sigungu;





}
