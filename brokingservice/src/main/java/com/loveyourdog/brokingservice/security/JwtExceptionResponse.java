package com.loveyourdog.brokingservice.security;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class JwtExceptionResponse {
    private String message;
    private HttpStatus httpStatus;


    public String convertToJson() throws JsonProcessingException {
        ObjectMapper  mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(this);
        return jsonString;
    }
}
