package br.com.emendes.timemanagerapi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@ToString
// TODO: Pensar nas valições de name, password e confirm
public class SignupRequest {

  @NotBlank
  private String name;
  @NotBlank
  @Email
  private String email;
  @NotBlank
  private String password;
  @NotBlank
  private String confirm;

}
