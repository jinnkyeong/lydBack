package com.loveyourdog.brokingservice.security;

import com.loveyourdog.brokingservice.model.entity.Admin;
import com.loveyourdog.brokingservice.model.entity.Customer;
import com.loveyourdog.brokingservice.repository.admin.AdminRepository;
import com.loveyourdog.brokingservice.repository.customer.CustomerRespository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminDetailsService implements UserDetailsService {
    private final AdminRepository adminRepository;




    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminRepository.findWithRolesBySign(username).orElseThrow(
                () -> new UsernameNotFoundException("Invalid authentication!")
        );
        return new AdminDetails(admin);
    }
}