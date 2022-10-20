package br.com.emendes.timemanagerapi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    SignupRequest that = (SignupRequest) o;

    if (!Objects.equals(name, that.name)) return false;
    if (!Objects.equals(email, that.email)) return false;
    if (!Objects.equals(password, that.password)) return false;
    return Objects.equals(confirm, that.confirm);
  }

  @Override
  public int hashCode() {
    int result = name != null ? name.hashCode() : 0;
    result = 31 * result + (email != null ? email.hashCode() : 0);
    result = 31 * result + (password != null ? password.hashCode() : 0);
    result = 31 * result + (confirm != null ? confirm.hashCode() : 0);
    return result;
  }
}
