package org.example.expert.config;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtUtil jwtUtil;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String bearerToken = request.getHeader("Authorization");
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      String jwt = jwtUtil.substringToken(bearerToken);
      try {
        Claims claims = jwtUtil.extractClaims(jwt);
        Long userId = Long.parseLong(claims.getSubject());
        String userRole = claims.get("userRole", String.class);

        // Authentication 객체를 생성하고 SecurityContext에 설정
        Authentication authentication = new UsernamePasswordAuthenticationToken(userId, null, List.of(new SimpleGrantedAuthority(userRole)));
        SecurityContextHolder.getContext().setAuthentication(authentication);
      } catch (Exception e) {
        // JWT 유효성 검사 실패 시 처리
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
        return;
      }
    }
    filterChain.doFilter(request, response);
  }
}
