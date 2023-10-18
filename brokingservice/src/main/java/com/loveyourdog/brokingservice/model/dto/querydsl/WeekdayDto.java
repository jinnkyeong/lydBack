package com.loveyourdog.brokingservice.model.dto.querydsl;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.lang.reflect.Field;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeekdayDto {
    private Long id;
    private String day;


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
