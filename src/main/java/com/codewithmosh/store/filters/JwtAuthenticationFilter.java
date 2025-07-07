package com.codewithmosh.store.filters;

import com.codewithmosh.store.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@AllArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private final JwtService jwtService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    var authHeader = request.getHeader("Authorization");
    if(authHeader == null || !authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    var token = authHeader.replace("Bearer ", "");
    if(!jwtService.validateToken(token)) {
      filterChain.doFilter(request, response);
      return;
    }

    var authentication = new UsernamePasswordAuthenticationToken(
      jwtService.getUserIdFromToken(token),
      null,
      null);

    // attach request details (like IP address, session ID)
    authentication.setDetails(
      new WebAuthenticationDetailsSource().buildDetails(request)
    );

    // store authentication in the security context
    SecurityContextHolder.getContext().setAuthentication(authentication);

    // proceed to next filter or controller
    filterChain.doFilter(request, response);
  }
}
