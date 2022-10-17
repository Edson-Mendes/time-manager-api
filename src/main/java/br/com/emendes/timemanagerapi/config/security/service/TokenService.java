package br.com.emendes.timemanagerapi.config.security.service;

import br.com.emendes.timemanagerapi.model.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenService {

  @Value("${timemanagerapi.jwt.expiration:3600000}")
  private String expiration;

  @Value("${timemanagerapi.jwt.secret:1234}")
  private String secret;

  public String generateToken(Authentication authentication) {
    User logged = (User) authentication.getPrincipal();
    Date now = new Date();
    Date expirationDate = new Date(now.getTime() + Long.parseLong(expiration));

    return Jwts.builder()
        .setIssuer("Time Manager API")
        .setSubject(logged.getId().toString())
        .setIssuedAt(now)
        .setExpiration(expirationDate)
        .signWith(SignatureAlgorithm.HS256, secret)
        .compact();
  }

  public boolean isTokenValid(String token) {
    try {
      Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public Long getUserId(String token) {
    Claims claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
    return Long.parseLong(claims.getSubject());
  }

}
