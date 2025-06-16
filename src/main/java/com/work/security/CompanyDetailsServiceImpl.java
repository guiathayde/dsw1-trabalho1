package com.work.security;

import com.work.models.Company;
import com.work.services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service("companyDetailsServiceImpl")
public class CompanyDetailsServiceImpl implements UserDetailsService {

    private final CompanyService companyService;

    @Autowired
    public CompanyDetailsServiceImpl(CompanyService companyService) {
        this.companyService = companyService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Company company = companyService.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Company not found with email: " + email));

        return User.builder()
                .username(company.getEmail())
                .password(company.getPassword())
                .authorities("ROLE_COMPANY") // Or .roles("COMPANY")
                .build();
    }
}
