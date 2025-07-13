package com.codewithmosh.store.auth.services;

import com.codewithmosh.store.users.entities.User;
import com.codewithmosh.store.users.repositories.UserRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;


@Getter
@Service
@Scope(value=WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class AuthService {
  private final User user;

  @Autowired
  public AuthService(UserRepository userRepository) {
    var userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    user = userRepository.findById(userId).orElseThrow();
  }

}
