package com.loveyourdog.brokingservice.model.dto.requestDto;

import lombok.*;

import java.lang.reflect.Field;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CusRequireRequestDto {

    private Long cusRequireId;


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
