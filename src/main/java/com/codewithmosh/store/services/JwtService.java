package com.codewithmosh.store.services;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

  @Value("${spring.jwt.secret}")
  private String secret;

  public String generateToken(String email) {
    var tokenExpirationInSec = 86400; // ~ 1 day
    System.out.println("Using secret : " + secret);
    return Jwts.builder()
      .subject(email)
      .issuedAt(new Date())
      .expiration(new Date(System.currentTimeMillis() + tokenExpirationInSec * 1000))
      .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
      .compact();
  }

  public boolean validateToken(String token) {
    try {
      var claims = Jwts.parser()
        .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
        .build()
        .parseSignedClaims(token)
        .getPayload();

      return claims.getExpiration().after(new Date());
    }
    catch (JwtException exp) {
      return false;
    }
  }
}
