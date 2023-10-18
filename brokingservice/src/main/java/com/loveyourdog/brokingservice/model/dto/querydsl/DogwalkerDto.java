package com.loveyourdog.brokingservice.model.dto.querydsl;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;

@Getter
@Setter
public class DogwalkerDto { // 임시
    private String email;

    private String pwd;

    private String name;

    @QueryProjection
    public DogwalkerDto(String email, String pwd, String name) {
        this.email = email;
        this.pwd = pwd;
        this.name = name;
    }


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
