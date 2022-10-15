package br.com.emendes.timemanagerapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponse {

  private String type;
  private String token;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    TokenResponse that = (TokenResponse) o;

    if (!Objects.equals(type, that.type)) return false;
    return Objects.equals(token, that.token);
  }

  @Override
  public int hashCode() {
    int result = type != null ? type.hashCode() : 0;
    result = 31 * result + (token != null ? token.hashCode() : 0);
    return result;
  }
}
