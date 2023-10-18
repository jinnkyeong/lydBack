package com.loveyourdog.brokingservice.security;

import com.loveyourdog.brokingservice.model.entity.Dogwalker;
import com.loveyourdog.brokingservice.repository.dogwalker.DogwalkerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DogwalkerDetailsService implements UserDetailsService {

    private final DogwalkerRepository dogwalkerRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("loadUserByUsername 실행 / username : "+username);
        Dogwalker dogwalker = dogwalkerRepository.findWithRolesByEmail(username).orElseThrow(
                () -> new UsernameNotFoundException("Invalid authentication!")
        );

        return new DogwalkerDetails(dogwalker);
    }
}