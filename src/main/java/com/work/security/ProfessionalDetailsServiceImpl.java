package com.work.security;

import com.work.models.Professional;
import com.work.services.ProfessionalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service("professionalDetailsServiceImpl")
public class ProfessionalDetailsServiceImpl implements UserDetailsService {

    private final ProfessionalService professionalService;

    @Autowired
    public ProfessionalDetailsServiceImpl(ProfessionalService professionalService) {
        this.professionalService = professionalService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Professional professional = professionalService.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Professional not found with email: " + email));

        return User.builder()
                .username(professional.getEmail())
                .password(professional.getPassword())
                .authorities("ROLE_PROFESSIONAL") // Or .roles("PROFESSIONAL")
                .build();
    }
}
