package com.loveyourdog.brokingservice.security;

import com.loveyourdog.brokingservice.model.entity.Customer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class CustomerDetails implements UserDetails {

    private final Customer customer;

    public CustomerDetails(Customer customer) {
//        System.out.println("CustomUserDetails 생성");
        this.customer = customer;
    }

    public final Customer getCustomer() {
        return customer;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return customer.getRoles().stream().map(o -> new SimpleGrantedAuthority(
                o.getName()
        )).collect(Collectors.toList());
//        return List.of(new SimpleGrantedAuthority("USER"));
    }

    @Override
    public String getPassword() {
        return customer.getPwd();
    }

    @Override
    public String getUsername() {
        return customer.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}