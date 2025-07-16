package com.codewithmosh.store.admin;

import com.codewithmosh.store.auth.entities.Role;
import com.codewithmosh.store.common.SecurityRules;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;

@Component
public class AdminSecurityRules implements SecurityRules {
  @Override
  public void configure(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry config) {
    config.requestMatchers(HttpMethod.GET, "/admin/hello")
      .hasRole(Role.ADMIN.name());
  }
}
