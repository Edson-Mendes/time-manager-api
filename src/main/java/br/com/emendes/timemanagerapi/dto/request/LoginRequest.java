package br.com.emendes.timemanagerapi.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LoginRequest {

  private String email;
  private String password;

}
