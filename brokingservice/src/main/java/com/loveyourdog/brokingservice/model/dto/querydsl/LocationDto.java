package com.loveyourdog.brokingservice.model.dto.querydsl;

import com.loveyourdog.brokingservice.model.entity.Location;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.lang.reflect.Field;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationDto {
    private Long id;
    private String state; // 시도
    private String town; // 시군구


    public Location toLocation(){
        Location location = Location.builder()
                .state(state)
                .town(town)
                .build();
        return location;
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
//
//    @QueryProjection
//
//    public LocationDto(Long id, String state, String town) {
//        this.id = id;
//        this.state = state;
//        this.town = town;
//    }
}
