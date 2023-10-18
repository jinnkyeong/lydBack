package com.loveyourdog.brokingservice.model.dto.querydsl;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;

@Getter
@Setter
@Builder
public class ZipcodeDto {
    private Long id;
    private String sido;
    private String sigungu;



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
