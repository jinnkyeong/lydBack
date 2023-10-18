package com.loveyourdog.brokingservice.security;

import com.loveyourdog.brokingservice.model.entity.Admin;
import com.loveyourdog.brokingservice.model.entity.Customer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class AdminDetails implements UserDetails {

    private final Admin admin;

    public AdminDetails(Admin admin) {
        this.admin = admin;
    }


    public final Admin getAdmin() {
        return admin;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return admin.getRoles().stream().map(o -> new SimpleGrantedAuthority(
                o.getName()
        )).collect(Collectors.toList());
//        return List.of(new SimpleGrantedAuthority("USER"));
    }

    @Override
    public String getPassword() {
        return admin.getPwd();
    }

    @Override
    public String getUsername() {
        return admin.getSign();
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