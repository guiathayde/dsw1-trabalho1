package br.ufscar.dc.dsw.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain; 

import br.ufscar.dc.dsw.security.UsuarioDetailsServiceImpl;

 @Configuration @EnableWebSecurity
public class WebSecurityConfig {

  @Autowired
  private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

  @Autowired
  private UsuarioDetailsServiceImpl usuarioDetailsServiceImpl;

  @Bean
  public static BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth, BCryptPasswordEncoder passwordEncoder) throws Exception {
    auth.userDetailsService(usuarioDetailsServiceImpl).passwordEncoder(passwordEncoder);
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
      .authorizeHttpRequests(auth -> auth
          .requestMatchers("/webjars/**", "/css/**", "/image/**", "/js/**").permitAll()
          .requestMatchers("/vagasPublicas/listar", "/", "/home", "/error", "/login/**").permitAll()
          .requestMatchers("/perfilAdministrador", "/profissionais/cadastrar", "/usuarios/novo", "/profissionais/salvar", "/profissionais/editar", "/profissionais/listar", "/profissionais/excluir/**").hasRole("ADMIN")
          .requestMatchers("/empresas/cadastrar", "/empresas/salvar", "/empresas/listar", "/empresas/editar/**", "/empresas/excluir/**").hasAnyRole("ADMIN", "EMPRESA")

          .requestMatchers("/vagas/cadastrar", "/vagas/salvar", "/vagas/minhasVagas", "/vagas/excluir/**", "/perfilEmpresa").hasRole("EMPRESA")
          .requestMatchers("/candidaturas/gerenciar/**", "/candidaturas/atualizarStatus").hasRole("EMPRESA")

          .requestMatchers("/candidaturas/candidatar/**", "/perfilProfissional","/candidaturas/minhasCandidaturas", "/candidaturas/salvar/**").hasRole("PROFISSIONAL")
          .requestMatchers("/api/**").permitAll()
          .anyRequest().authenticated()
        )
        .csrf(csrf -> csrf.disable()) // Desabilita CSRF para simplificar o exemplo
        .formLogin(form -> form
          .loginPage("/login") 
          .successHandler(customAuthenticationSuccessHandler)
          .failureUrl("/erro?msg=Usuario ou senha invalida") 
          .permitAll()
        )
        .logout(logout -> logout
          .logoutSuccessUrl("/") 
          .permitAll()
        )
        .exceptionHandling(exceptions -> exceptions
          .accessDeniedPage("/acessoNegado") 
        );
    return http.build();
  }
}