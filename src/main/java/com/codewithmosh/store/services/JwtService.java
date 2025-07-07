package com.codewithmosh.store.services;

import com.codewithmosh.store.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

  @Value("${spring.jwt.secret}")
  private String secret;

  public String generateToken(User user) {
    var tokenExpirationInSec = 86400; // ~ 1 day
    return Jwts.builder()
      .subject(user.getId().toString())
      .claims(Map.of("name", user.getName(), "email", user.getEmail()))
      .issuedAt(new Date())
      .expiration(new Date(System.currentTimeMillis() + tokenExpirationInSec * 1000))
      .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
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

  private Claims getClaims(String token) {
    return Jwts.parser()
      .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
      .build()
      .parseSignedClaims(token)
      .getPayload();
  }
}
