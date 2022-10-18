package br.com.emendes.timemanagerapi.dto.request;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoginRequest {

  @NotBlank(message = "email must not be null or blank")
  @Email(message = "must be a well-formed email address")
  private String email;
  @NotBlank(message = "password must not be null or blank")
  private String password;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    LoginRequest that = (LoginRequest) o;

    if (!Objects.equals(email, that.email)) return false;
    return Objects.equals(password, that.password);
  }

  @Override
  public int hashCode() {
    int result = email != null ? email.hashCode() : 0;
    result = 31 * result + (password != null ? password.hashCode() : 0);
    return result;
  }
}
