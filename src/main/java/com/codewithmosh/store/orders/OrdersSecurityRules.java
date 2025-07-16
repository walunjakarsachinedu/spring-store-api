package com.codewithmosh.store.orders;

import com.codewithmosh.store.common.SecurityRules;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;

@Component
public class OrdersSecurityRules implements SecurityRules {
  @Override
  public void configure(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry config) {
    config.requestMatchers("/orders/**").authenticated();
  }
}
