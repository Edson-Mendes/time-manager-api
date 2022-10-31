package br.com.emendes.timemanagerapi.controller;

import br.com.emendes.timemanagerapi.controller.openapi.SignupControllerOpenApi;
import br.com.emendes.timemanagerapi.dto.request.SignupRequest;
import br.com.emendes.timemanagerapi.dto.response.UserResponse;
import br.com.emendes.timemanagerapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("/signup")
public class SignupController implements SignupControllerOpenApi {

  private final UserService userService;

  @Override
  @PostMapping
  public ResponseEntity<UserResponse> signup(@RequestBody @Valid SignupRequest signupRequest, UriComponentsBuilder uriBuilder){
    UserResponse userResponse = userService.save(signupRequest);
    URI uri = uriBuilder.path("/users/{id}").build(userResponse.getId());
    return ResponseEntity.created(uri).body(userResponse);
  }

}
