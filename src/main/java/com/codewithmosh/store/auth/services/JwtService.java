package com.codewithmosh.store.auth.services;

import com.codewithmosh.store.auth.entities.Jwt;
import com.codewithmosh.store.auth.config.JwtConfig;
import com.codewithmosh.store.users.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@AllArgsConstructor
@Service
public class JwtService {
  private final JwtConfig jwtConfig;

  public Jwt generateAccessToken(User user) {
    return generateToken(user, jwtConfig.getAccessTokenExpiration());
  }

  public Jwt generateRefreshToken(User user) {
    return generateToken(user, jwtConfig.getRefreshTokenExpiration());
  }

  private Jwt generateToken(User user, long tokenExpirationInSec) {
    var claims = Jwts.claims()
      .subject(user.getId().toString())
      .add("name", user.getName())
      .add("email", user.getEmail())
      .add("role", user.getRole())
      .issuedAt(new Date())
      .expiration(new Date(System.currentTimeMillis() + tokenExpirationInSec * 1000))
      .build();

    return new Jwt(claims, jwtConfig.getSecretKey());
  }

  public Jwt parseToken(String token) {
    try {
      var claims = getClaims(token);
      return new Jwt(claims, jwtConfig.getSecretKey());
    }
    catch (JwtException exception) {
      return null;
    }
  }

  private Claims getClaims(String token) {
    return Jwts.parser()
      .verifyWith(jwtConfig.getSecretKey())
      .build()
      .parseSignedClaims(token)
      .getPayload();
  }
}
