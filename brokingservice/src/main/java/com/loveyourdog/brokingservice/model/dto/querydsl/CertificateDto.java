package com.loveyourdog.brokingservice.model.dto.querydsl;

import com.loveyourdog.brokingservice.model.entity.Dogwalker;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;

@Getter
@Setter
@Builder
public class CertificateDto {
    private Long id;
    private String keyword;
//    private String info;
//    private Dogwalker dogwalker; // 맞나?



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

    @QueryProjection
    public CertificateDto(Long id, String keyword) {
        this.id = id;
        this.keyword = keyword;
    }
}
