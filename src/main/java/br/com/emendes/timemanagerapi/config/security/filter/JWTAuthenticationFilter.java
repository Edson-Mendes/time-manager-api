package br.com.emendes.timemanagerapi.config.security.filter;

import br.com.emendes.timemanagerapi.config.security.service.AuthenticationService;
import br.com.emendes.timemanagerapi.config.security.service.TokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTAuthenticationFilter extends BasicAuthenticationFilter {

  private final TokenService tokenService;
  private final AuthenticationService authenticationService;

  public JWTAuthenticationFilter(
      AuthenticationManager authenticationManager, TokenService tokenService, AuthenticationService authenticationService) {
    super(authenticationManager);
    this.authenticationService = authenticationService;
    this.tokenService = tokenService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
    String token = recoverToken(request);
    if (tokenService.isTokenValid(token)) {
      SecurityContextHolder.getContext().setAuthentication(getAuthentication(token));
    }

    chain.doFilter(request, response);
  }

  private String recoverToken(HttpServletRequest request) {
    String token = request.getHeader("Authorization");
    if (token == null || !token.startsWith("Bearer ")) {
      return null;
    }
    return token.substring(7);
  }

  private Authentication getAuthentication(String token) {
    Long userId = tokenService.getUserId(token);
    UserDetails userDetails = authenticationService.findUserDetailsById(userId);
    return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
  }

}
