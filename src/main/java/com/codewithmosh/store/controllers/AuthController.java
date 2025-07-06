package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.LoginRequest;
import com.codewithmosh.store.exceptions.InvalidCredentialException;
import com.codewithmosh.store.repositories.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;

  @PostMapping("/login")
  public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
    var user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(InvalidCredentialException::new);

    var isPasswordMatch = passwordEncoder.matches(loginRequest.getPassword(), user.getPassword());
    if(!isPasswordMatch) throw new InvalidCredentialException();

    return ResponseEntity.ok().build();
  }

  @ExceptionHandler(InvalidCredentialException.class)
  public ResponseEntity<?> invalidCredentialHandler() {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
  }
}
