package com.codewithmosh.store.auth.config;

import com.codewithmosh.store.auth.JwtAuthenticationFilter;
import com.codewithmosh.store.common.LoggingFilter;
import com.codewithmosh.store.common.SecurityRules;
import com.codewithmosh.store.users.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@AllArgsConstructor
@Configuration // mark class as bean
@EnableWebSecurity
public class SecurityConfig {
  final private UserService userService;
  final private JwtAuthenticationFilter jwtAuthenticationFilter;
  final private LoggingFilter loggingFilter;
  final private List<SecurityRules> securityRules;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    var provider = new DaoAuthenticationProvider();
    provider.setPasswordEncoder(passwordEncoder());
    provider.setUserDetailsService(userService);
    return provider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
      .sessionManagement(c ->
        c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .csrf(AbstractHttpConfigurer::disable);

    http.authorizeHttpRequests(c -> securityRules.forEach(rule -> rule.configure(c)));

   return http
      .authorizeHttpRequests(c -> c.anyRequest().authenticated())
     .exceptionHandling(c -> c
       .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
       .accessDeniedHandler((req, res, ex) -> {
         res.setStatus(403);
         res.setContentType("application/json");
         res.getWriter().write("{\"error\": \"Forbidden\"}");
         res.getWriter().flush();
       })
     )
     .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
     .addFilterBefore(loggingFilter, JwtAuthenticationFilter.class)
     .build();
  }
}
