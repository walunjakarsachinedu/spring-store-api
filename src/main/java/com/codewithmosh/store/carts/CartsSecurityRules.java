package com.codewithmosh.store.carts;

import com.codewithmosh.store.auth.entities.Role;
import com.codewithmosh.store.common.SecurityRules;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;

@Component
public class CartsSecurityRules implements SecurityRules {
  @Override
  public void configure(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry config) {
      config
        .requestMatchers(HttpMethod.POST, "/carts/**").permitAll()
        .requestMatchers(HttpMethod.PUT, "/carts/**").permitAll()
        .requestMatchers(HttpMethod.GET, "/carts/**").permitAll()
        .requestMatchers(HttpMethod.DELETE, "/carts/**").hasRole(Role.ADMIN.name());
  }
}
