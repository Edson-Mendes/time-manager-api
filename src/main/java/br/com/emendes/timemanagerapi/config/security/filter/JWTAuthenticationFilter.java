package br.com.emendes.timemanagerapi.config.security.filter;

import br.com.emendes.timemanagerapi.dto.request.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

// TODO: Deletar filtro
@Log4j2
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final AuthenticationManager authenticationManager;

  public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
    setPostOnly(true);
    setFilterProcessesUrl("/login");
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

    try {
      LoginRequest creds = new ObjectMapper()
          .readValue(request.getInputStream(), LoginRequest.class);

      log.debug(creds);

      return authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getPassword())
      );
    } catch (IOException e) {
      throw new RuntimeException("Deu ruim na LEITURA do body");
    }
  }

  @Override
  protected void successfulAuthentication(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain, Authentication auth) throws IOException, ServletException {
    UserDetails logged = (UserDetails) auth.getPrincipal();
    Date now = new Date();
    Date expirationDate = new Date(now.getTime() + 3600000L);

    String token = Jwts.builder()
        .setIssuer("Time Manager API")
        .setSubject(logged.getUsername())
        .setIssuedAt(now)
        .setExpiration(expirationDate)
        .signWith(SignatureAlgorithm.HS256, "1234")
        .compact();

    String body = logged.getUsername() + " " + token;

    response.getWriter().write(body);
    response.getWriter().flush();
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
//    super.unsuccessfulAuthentication(request, response, failed);
    response.setStatus(400);
    response.getWriter().write("Credenciais inválidas!!!");
    response.getWriter().flush();
  }
}
