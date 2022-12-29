package br.com.emendes.timemanagerapi.config.security.service;

import org.springframework.security.core.Authentication;

public interface TokenService {

  String generateToken(Authentication authentication);

  boolean isTokenValid(String token);

  Long getUserId(String token);

}
