package br.com.emendes.timemanagerapi.controller;

import br.com.emendes.timemanagerapi.dto.request.SignupRequest;
import br.com.emendes.timemanagerapi.dto.response.UserResponse;
import br.com.emendes.timemanagerapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/signup")
public class SignupController {

  private final UserService userService;

  @PostMapping
  public ResponseEntity<UserResponse> signup(@RequestBody @Valid SignupRequest signupRequest){
    return ResponseEntity.ok(userService.save(signupRequest));
  }

}
