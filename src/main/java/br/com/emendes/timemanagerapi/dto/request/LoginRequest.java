package br.com.emendes.timemanagerapi.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
public class LoginRequest {

  @NotBlank
  private String email;
  @NotBlank
  private String password;

}
