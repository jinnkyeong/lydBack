package com.loveyourdog.brokingservice.security;

import com.loveyourdog.brokingservice.model.entity.Dogwalker;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class DogwalkerDetails implements UserDetails {

    private final Dogwalker dogwalker;

    public DogwalkerDetails(Dogwalker dogwalker) {
        this.dogwalker = dogwalker;
    }

    public final Dogwalker getDogwalker() {
        return dogwalker;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return dogwalker.getRoles()
                .stream()
                .map(o -> new SimpleGrantedAuthority(o.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return dogwalker.getPwd();
    }

    @Override
    public String getUsername() {
        return dogwalker.getEmail();
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