package br.com.emendes.timemanagerapi.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserResponse {

  private Long id;
  private String name;
  private String email;

}
