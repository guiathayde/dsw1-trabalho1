package com.work.security;

import com.work.models.Admin;
import com.work.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList; // Or Collections.singletonList

@Service("adminDetailsServiceImpl")
public class AdminDetailsServiceImpl implements UserDetailsService {

    private final AdminService adminService;

    @Autowired
    public AdminDetailsServiceImpl(AdminService adminService) {
        this.adminService = adminService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Admin admin = adminService.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Admin not found with email: " + email));

        // Spring Security's User requires roles to be prefixed with "ROLE_"
        return User.builder()
                .username(admin.getEmail())
                .password(admin.getPassword())
                .authorities("ROLE_ADMIN") // Or .roles("ADMIN") which adds "ROLE_" automatically
                .build();
    }
}
