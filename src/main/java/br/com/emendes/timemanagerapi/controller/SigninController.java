package br.com.emendes.timemanagerapi.controller;

import br.com.emendes.timemanagerapi.dto.request.LoginRequest;
import br.com.emendes.timemanagerapi.dto.response.TokenResponse;
import br.com.emendes.timemanagerapi.service.SigninService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/signin")
public class SigninController {

  private final SigninService signinService;

  @PostMapping
  public ResponseEntity<TokenResponse> signin(@RequestBody @Valid LoginRequest loginRequest) {
    return ResponseEntity.ok(signinService.signin(loginRequest));
  }

}
