package com.loveyourdog.brokingservice.security;

import com.loveyourdog.brokingservice.model.entity.Customer;
import com.loveyourdog.brokingservice.repository.customer.CustomerRespository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerDetailsService implements UserDetailsService {
    private final CustomerRespository customerRespository;




    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("loadUserByUsername 실행 / username : "+username);
        Customer customer = customerRespository.findWithRolesByEmail(username).orElseThrow(
                () -> new UsernameNotFoundException("Invalid authentication!")
        );
        return new CustomerDetails(customer);
    }
}