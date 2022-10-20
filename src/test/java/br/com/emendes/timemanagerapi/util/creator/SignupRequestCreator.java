package br.com.emendes.timemanagerapi.util.creator;

import br.com.emendes.timemanagerapi.dto.request.SignupRequest;

public class SignupRequestCreator {

  public static SignupRequest withNameEmailAndPassword(String name, String email, String password) {
    return new SignupRequest(name, email, password, password);
  }

}
