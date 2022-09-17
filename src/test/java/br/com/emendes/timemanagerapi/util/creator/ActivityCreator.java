package br.com.emendes.timemanagerapi.util.creator;

import br.com.emendes.timemanagerapi.model.Activity;

import java.time.LocalDateTime;

public class ActivityCreator {

  private static final LocalDateTime FIXED_DATE_TIME = LocalDateTime
      .of(2022, 8, 23, 10,40,21);

  public static Activity withIdAndName(Long id, String name){
    return Activity.builder()
        .id(id)
        .name(name)
        .description("A simple project for my portfolio")
        .createdAt(FIXED_DATE_TIME)
        .enabled(true)
        .concluded(false)
        .build();
  }

  public static Activity withIdNameAndDescription(long id, String name, String description) {
    return Activity.builder()
        .id(id)
        .name(name)
        .description(description)
        .createdAt(FIXED_DATE_TIME)
        .enabled(true)
        .concluded(false)
        .build();
  }

  public static Activity withoutIdAndWithNameAndDescription(String name, String description){
    return Activity.builder()
        .name(name)
        .description(description)
        .createdAt(LocalDateTime.now())
        .enabled(true)
        .concluded(false)
        .build();
  }

}
