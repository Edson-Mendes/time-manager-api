package br.com.emendes.timemanagerapi.util.creator;

import br.com.emendes.timemanagerapi.dto.request.LoginRequest;

public class LoginRequestCreator {

  public static LoginRequest validLoginRequest(){
    return new LoginRequest("user@email.com", "123456");
  }

}
