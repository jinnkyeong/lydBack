package com.loveyourdog.brokingservice.repository.application;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Getter
@NoArgsConstructor

public class ApplicationOrderCondition {

    private String direction;
    private String properties;

    public void setDirection(String direction) {
        this.direction = StringUtils.hasText(direction)? direction : "DESC";
    }

    // price, star, view
    public void setProperties(String properties) {
        this.properties = StringUtils.hasText(properties)? properties : "view";
    }
}