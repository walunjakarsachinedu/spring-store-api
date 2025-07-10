package com.codewithmosh.store.services;

import com.codewithmosh.store.config.JwtConfig;
import com.codewithmosh.store.entities.Role;
import com.codewithmosh.store.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@AllArgsConstructor
@Service
public class JwtService {
  private final JwtConfig jwtConfig;

  public String generateAccessToken(User user) {
    return generateToken(user, jwtConfig.getAccessTokenExpiration());
  }

  public String generateRefreshToken(User user) {
    return generateToken(user, jwtConfig.getRefreshTokenExpiration());
  }

  private String generateToken(User user, long tokenExpirationInSec) {
    return Jwts.builder()
      .subject(user.getId().toString())
      .claims(Map.of(
        "name", user.getName(),
        "email", user.getEmail(),
        "role", user.getRole()
      ))
      .issuedAt(new Date())
      .expiration(new Date(System.currentTimeMillis() + tokenExpirationInSec * 1000))
      .signWith(jwtConfig.getSecretKey())
      .compact();
  }

  public boolean validateToken(String token) {
    try {
      return getClaims(token).getExpiration().after(new Date());
    }
    catch (JwtException exp) {
      return false;
    }
  }

  public Long getUserIdFromToken(String token) {
    return Long.valueOf(getClaims(token).getSubject());
  }

  public Role getRoleFromToken(String token) {
    return Role.valueOf(getClaims(token).get("role", String.class));
  }

  private Claims getClaims(String token) {
    return Jwts.parser()
      .verifyWith(jwtConfig.getSecretKey())
      .build()
      .parseSignedClaims(token)
      .getPayload();
  }
}
