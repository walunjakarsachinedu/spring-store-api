package com.codewithmosh.store.auth;

import com.codewithmosh.store.auth.config.JwtConfig;
import com.codewithmosh.store.auth.dtos.JwtResponse;
import com.codewithmosh.store.auth.dtos.LoginRequest;
import com.codewithmosh.store.auth.services.JwtService;
import com.codewithmosh.store.users.UserMapper;
import com.codewithmosh.store.users.repositories.UserRepository;
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
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
  private final UserRepository userRepository;
  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;
  private final UserMapper userMapper;
  private final JwtConfig jwtConfig;

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
    System.out.println("fetching user from database");

    var accessToken = jwtService.generateRefreshToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);

    var cookie = new Cookie("refreshToken", refreshToken.toString());
    cookie.setPath("/auth/refresh");
    cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration());
    cookie.setHttpOnly(true);
    cookie.setSecure(true);
    response.addCookie(cookie);

    return ResponseEntity.ok(
      new JwtResponse(accessToken.toString())
    );
  }

  @PostMapping("/refresh")
  public ResponseEntity<JwtResponse> refresh(
    @CookieValue("refreshToken") String refreshToken
  ) {
    var jwt = jwtService.parseToken(refreshToken);
    if(jwt == null || jwt.isExpired()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    var userId = jwt.getUserId();
    var user = userRepository.findById(userId).orElseThrow();
    var accessToken = jwtService.generateAccessToken(user);

    return ResponseEntity.ok(new JwtResponse(accessToken.toString()));
  }

  @GetMapping("/me")
  public ResponseEntity<?> me() {
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    var userId = (Long) authentication.getPrincipal();

    var user = userRepository.findById(userId).orElseThrow();

    var userDto = userMapper.toDto(user);
    return ResponseEntity.ok(userDto);
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<Void> handleBadCredentialException() {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
  }
}
