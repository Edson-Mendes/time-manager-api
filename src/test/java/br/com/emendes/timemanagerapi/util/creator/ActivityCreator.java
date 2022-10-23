package br.com.emendes.timemanagerapi.util.creator;

import br.com.emendes.timemanagerapi.model.entity.Activity;
import br.com.emendes.timemanagerapi.model.Status;
import br.com.emendes.timemanagerapi.model.entity.User;

import java.time.LocalDateTime;
import java.util.Optional;

public class ActivityCreator {

  private static final LocalDateTime FIXED_DATE_TIME = LocalDateTime
      .of(2022, 8, 23, 10,40,21);

  public static Activity withIdAndName(Long id, String name){
    return Activity.builder()
        .id(id)
        .name(name)
        .description("A simple project for my portfolio")
        .createdAt(FIXED_DATE_TIME)
        .status(Status.ACTIVE)
        .build();
  }

  public static Activity withIdNameAndDescription(long id, String name, String description) {
    return Activity.builder()
        .id(id)
        .name(name)
        .description(description)
        .createdAt(FIXED_DATE_TIME)
        .status(Status.ACTIVE)
        .build();
  }

  public static Activity withoutIdAndWithNameAndDescription(String name, String description){
    User user = new User();
    user.setId(1L);
    return Activity.builder()
        .name(name)
        .description(description)
        .createdAt(LocalDateTime.now())
        .status(Status.ACTIVE)
        .user(user)
        .build();
  }

  public static Activity withStatus(Status status) {
    User user = new User();
    user.setId(1L);
    return Activity.builder()
        .name("Lorem Activity")
        .description("some description")
        .createdAt(LocalDateTime.parse("2022-10-03T10:15:30"))
        .status(status)
        .user(user)
        .build();
  }

  public static Activity withIdAndStatus(long id, Status status) {
    return Activity.builder()
        .id(id)
        .name("Lorem Activity")
        .description("some description")
        .createdAt(LocalDateTime.parse("2022-10-03T10:15:30"))
        .status(status)
        .build();
  }
}
