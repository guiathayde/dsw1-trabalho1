package com.work.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    @Qualifier("adminDetailsServiceImpl")
    private UserDetailsService adminUserDetailsService;

    @Autowired
    @Qualifier("professionalDetailsServiceImpl")
    private UserDetailsService professionalUserDetailsService;

    @Autowired
    @Qualifier("companyDetailsServiceImpl")
    private UserDetailsService companyUserDetailsService;

    // Common configuration for AuthenticationManagerBuilder
    private void configureGlobalAuth(AuthenticationManagerBuilder auth, UserDetailsService userDetailsService) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    // Separate AuthenticationManagers might be needed if login pages are truly distinct
    // and you need different auth providers for different login forms.
    // For now, we aim for a primary one and handle others via distinct login processing URLs.

    @Bean
    @Order(1) // Admin security first
    public SecurityFilterChain adminFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/admin/**", "/admin-login", "/h2-console/**") // Apply this filter chain only to admin paths
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/admin/login", "/admin-login", "/css/**", "/js/**", "/images/**", "/h2-console/**").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated() // Or deny if not matched by other filter chains
            )
            .formLogin(form -> form
                .loginPage("/admin-login") // Custom admin login page
                .loginProcessingUrl("/admin-login") // URL to submit the username and password to
                .defaultSuccessUrl("/admin/dashboard", true) // Redirect after successful login
                .failureUrl("/admin-login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/admin/logout"))
                .logoutSuccessUrl("/admin-login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .userDetailsService(adminUserDetailsService) // Important: associate UserDetailsService
            .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**")) // Disable CSRF for H2 console
            .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin())); // Allow H2 console framing

        return http.build();
    }

    @Bean
    @Order(2) // Company security
    public SecurityFilterChain companyFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/company/**", "/company-login")
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/company/login", "/company-login", "/company/register", "/css/**", "/js/**", "/images/**").permitAll()
                .requestMatchers("/company/**").hasRole("COMPANY")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/company-login")
                .loginProcessingUrl("/company-login")
                .defaultSuccessUrl("/company/dashboard", true)
                .failureUrl("/company-login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/company/logout"))
                .logoutSuccessUrl("/company-login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .userDetailsService(companyUserDetailsService); // Associate UserDetailsService

        return http.build();
    }

    @Bean
    @Order(3) // Professional security
    public SecurityFilterChain professionalFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/professional/**", "/professional-login")
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/professional/login", "/professional-login", "/professional/register", "/css/**", "/js/**", "/images/**").permitAll()
                .requestMatchers("/professional/**").hasRole("PROFESSIONAL")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/professional-login")
                .loginProcessingUrl("/professional-login")
                .defaultSuccessUrl("/professional/dashboard", true)
                .failureUrl("/professional-login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/professional/logout"))
                .logoutSuccessUrl("/professional-login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .userDetailsService(professionalUserDetailsService); // Associate UserDetailsService

        return http.build();
    }

    @Bean
    @Order(4) // General/Public access & other specific authentications
    public SecurityFilterChain defaultFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/", "/home", "/vacancies/**", "/register", "/css/**", "/js/**", "/images/**", "/error/**", "/public/**").permitAll()
                // Any other request not matched by previous filter chains will require authentication by default
                // or you can specify .anyRequest().denyAll() if all authenticated access is via specific chains.
                .anyRequest().authenticated()
            )
            // This default chain might have a generic login page or rely on specific login pages from other chains.
            // If a user tries to access a protected page not covered by /admin, /company, or /professional,
            // Spring Security needs a login mechanism.
            // For simplicity, we'll assume most authenticated users go through one of the specific logins.
            // If a general login is needed:
            // .formLogin(form -> form
            //     .loginPage("/login") // A general login page
            //     .defaultSuccessUrl("/welcome", true)
            //     .permitAll()
            // )
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // General logout
                .logoutSuccessUrl("/?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            // This is tricky with multiple UserDetailServices.
            // One approach is to have a composite UserDetailsService or a custom AuthenticationProvider.
            // For now, this default chain won't explicitly set one, relying on specific chains for primary auth.
            // If a request hits this chain and requires authentication, it might fail if no UserDetailsService is configured for its pattern.
            // This setup implies that users must use one of the specific login portals.
             .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**")) // If H2 is used and not covered by admin
             .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin())); // If H2 is used


        return http.build();
    }

    // Global AuthenticationManager (optional if using HttpSecurity.userDetailsService directly in each chain)
    // This bean is often needed if you want to expose AuthenticationManager (e.g., for programmatic login).
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        // This is a common way to get the AuthenticationManager.
        // It will be configured by the HttpSecurity instances.
        // You might need to be more specific if you have multiple AuthenticationManagerBuilder instances.
        // For this multi-UserDetailsService setup, it's simpler to let each SecurityFilterChain
        // manage its own AuthenticationProvider (implicitly via userDetailsService).
        // If a single AuthenticationManager is strictly needed, a custom one might be required.
        // For now, we rely on each filter chain's config.
        return http.getSharedObject(AuthenticationManagerBuilder.class).build();
    }
}
