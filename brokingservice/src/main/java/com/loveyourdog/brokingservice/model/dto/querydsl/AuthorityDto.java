package com.loveyourdog.brokingservice.model.dto.querydsl;

import lombok.*;

import java.lang.reflect.Field;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorityDto {

    private Long id;
    private String name;
    private Long dogwalkerId;
    private Long customerId;
    private Long adminId;


//    모든 변수가 NULL 이면 true
//    하나라도 NULL이 아닌 변수가 있으면 false
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
//    @QueryProjection



}
