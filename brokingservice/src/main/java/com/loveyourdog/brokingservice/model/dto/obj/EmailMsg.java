package com.loveyourdog.brokingservice.model.dto.obj;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EmailMsg {
    private String to; // 수신자 email
    private String subject; //제목
    private String message; // 내용
}
