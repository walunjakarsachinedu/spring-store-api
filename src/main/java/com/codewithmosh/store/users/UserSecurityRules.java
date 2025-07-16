package com.codewithmosh.store.users;

import com.codewithmosh.store.auth.entities.Role;
import com.codewithmosh.store.common.SecurityRules;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;

@Component
public class UserSecurityRules implements SecurityRules {
  @Override
  public void configure(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry config) {
    config
      .requestMatchers(HttpMethod.POST, "/users").permitAll()
      .requestMatchers(HttpMethod.DELETE, "/users/*").hasRole(Role.ADMIN.name())
      .requestMatchers(HttpMethod.POST, "/users/*/change-password").permitAll();
  }
}
