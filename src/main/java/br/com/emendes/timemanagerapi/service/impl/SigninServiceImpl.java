package br.com.emendes.timemanagerapi.service.impl;

import br.com.emendes.timemanagerapi.config.security.service.TokenService;
import br.com.emendes.timemanagerapi.dto.request.LoginRequest;
import br.com.emendes.timemanagerapi.dto.response.TokenResponse;
import br.com.emendes.timemanagerapi.service.SigninService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SigninServiceImpl implements SigninService {

  private final AuthenticationManager authManager;
  private final TokenService tokenService;

  @Override
  public TokenResponse signin(LoginRequest loginRequest) {
    Authentication authentication = authManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
    String token = tokenService.generateToken(authentication);
    return new TokenResponse("Bearer", token);
  }

}
