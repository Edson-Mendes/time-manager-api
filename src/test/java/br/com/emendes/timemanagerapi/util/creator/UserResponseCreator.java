package br.com.emendes.timemanagerapi.util.creator;

import br.com.emendes.timemanagerapi.dto.response.UserResponse;

public class UserResponseCreator {

  public static UserResponse withNameAndEmail(String name, String email){
    return new UserResponse(1L, name, email);
  }

}
