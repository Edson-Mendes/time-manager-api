package br.com.emendes.timemanagerapi.service;

import br.com.emendes.timemanagerapi.dto.request.LoginRequest;
import br.com.emendes.timemanagerapi.dto.response.TokenResponse;

public interface SigninService {

  TokenResponse signin(LoginRequest loginRequest);

}
