package br.com.emendes.timemanagerapi.util.creator;

import br.com.emendes.timemanagerapi.model.entity.Role;
import br.com.emendes.timemanagerapi.model.entity.User;

import java.util.HashSet;
import java.util.Set;

public class UserCreator {

  public static User withoutId() {
    Set<Role> roles = new HashSet<>();
    roles.add(new Role(1, "ROLE_USER"));

    return User.builder()
        .name("user")
        .email("user@email.com")
        .password("123456")
        .roles(roles)
        .build();
  }

  public static User withAllParameters() {
    Set<Role> roles = new HashSet<>();
    roles.add(new Role(1, "ROLE_USER"));

    return User.builder()
        .id(1L)
        .name("user")
        .email("user@email.com")
        .password("123456")
        .roles(roles)
        .build();
  }

  public static User withNameAndEmail(String name, String email) {
    return User.builder()
        .name(name)
        .email(email)
        .build();
  }
}
