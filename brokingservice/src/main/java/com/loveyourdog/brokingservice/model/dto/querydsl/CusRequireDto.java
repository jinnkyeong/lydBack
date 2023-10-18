package com.loveyourdog.brokingservice.model.dto.querydsl;

import com.loveyourdog.brokingservice.model.entity.Commision;
import com.loveyourdog.brokingservice.model.entity.Reservation;
import lombok.*;

import java.lang.reflect.Field;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CusRequireDto {


    public Long id;
    public Long commisionId;
    public Long reservationId;

    public String context; // 내용
    public String dirName; // S3 객체 이름

    public String fileName; // 이미지 파일 이름

    public String extension; // 파일 확장자


//    public Commision commision;
//    public Reservation reservation;



    public boolean isDtoEntireVariableNull() {
        try {
            for (Field f : getClass().getDeclaredFields()) {
                if (f.get(this) != null) {
                    return false;
                }
            }
            return true;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
