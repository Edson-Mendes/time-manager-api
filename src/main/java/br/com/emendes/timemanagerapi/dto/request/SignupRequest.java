package br.com.emendes.timemanagerapi.dto.request;

import br.com.emendes.timemanagerapi.validation.anotation.PasswordMatch;
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
@PasswordMatch // TODO: Pesquisar como adicionar FieldErrors
// TODO: Realizar verificação de tamanho das Strings, fazer isso em todos os requests!
public class SignupRequest {

  @NotBlank(message = "name must not be null or blank")
  private String name;
  @NotBlank(message = "email must not be null or blank")
  @Email(message = "must be a well-formed email address")
  private String email;
  @NotBlank(message = "password must not be null or blank")
  private String password;
  @NotBlank(message = "confirm must not be null or blank")
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
