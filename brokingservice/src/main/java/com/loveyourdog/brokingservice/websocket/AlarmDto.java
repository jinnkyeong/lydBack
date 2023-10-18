package com.loveyourdog.brokingservice.websocket;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlarmDto {

    private String userId;
    private String userType; // dogwalker / customer

}
