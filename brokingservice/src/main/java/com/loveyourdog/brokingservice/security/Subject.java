package com.loveyourdog.brokingservice.security;

import lombok.Getter;

@Getter
public class Subject {

    private  String email;

    private  String tokenType;

    private String userType;

    private Subject(String email, String tokenType, String userType) {
        this.email = email;
        this.tokenType = tokenType;
        this.userType = userType;
    }
    private Subject(){

    }


    public static Subject atk(String email,String userType) {
        return new Subject(email, "ATK", userType);
    }

    public static Subject rtk(String email,String userType) {
        return new Subject(email, "RTK",userType);
    }
}