package com.loveyourdog.brokingservice.security;

import com.loveyourdog.brokingservice.model.entity.Customer;
import com.loveyourdog.brokingservice.model.entity.Dogwalker;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AccountResponse {


    private final String email;


    private AccountResponse(String email) {
        this.email = email;
    }

    public static AccountResponse dogWalkerToAccountRes(Dogwalker dogwalker) {
        return new AccountResponse(
                dogwalker.getEmail());
    }
    public static AccountResponse customerToAccountRes(Customer customer) {
        return new AccountResponse(
                customer.getEmail());
    }
}