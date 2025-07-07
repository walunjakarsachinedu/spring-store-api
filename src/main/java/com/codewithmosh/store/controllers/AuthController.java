package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.JwtResponse;
import com.codewithmosh.store.dtos.LoginRequest;
import com.codewithmosh.store.mappers.UserMapper;
import com.codewithmosh.store.repositories.UserRepository;
import com.codewithmosh.store.services.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;
  private final UserMapper userMapper;

  @PostMapping("/login")
  public ResponseEntity<JwtResponse> login(
    @Valid @RequestBody LoginRequest loginRequest,
    HttpServletResponse response
  ) {
    authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(
        loginRequest.getEmail(),
        loginRequest.getPassword()
      )
    );

    var user = userRepository.findByEmail(loginRequest.getEmail())
      .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    var refreshToken = jwtService.generateRefreshToken(user);

    var cookie = new Cookie("refreshToken", refreshToken);
    cookie.setPath("/auth/refresh");
    cookie.setMaxAge(7 * 86400); // ~ 7 days
    cookie.setHttpOnly(true);
    cookie.setSecure(true);
    response.addCookie(cookie);

    return ResponseEntity.ok(
      new JwtResponse(jwtService.generateAccessToken(user))
    );
  }

  @GetMapping("/me")
  public ResponseEntity<?> me() {
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    var userId = (Long) authentication.getPrincipal();

    var user = userRepository.findById(userId).orElseThrow();

    var userDto = userMapper.toDto(user);
    return ResponseEntity.ok(userDto);
  }

  @PostMapping("/validate")
  public boolean validate(@RequestHeader("Authorization") String authHeader) {
    var token = authHeader.replace("Bearer ", "");
    return jwtService.validateToken(token);
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<Void> handleBadCredentialException() {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
  }
}
