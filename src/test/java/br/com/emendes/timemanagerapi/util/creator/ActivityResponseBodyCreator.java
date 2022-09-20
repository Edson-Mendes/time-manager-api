package br.com.emendes.timemanagerapi.util.creator;

import br.com.emendes.timemanagerapi.dto.response.ActivityResponseBody;
import br.com.emendes.timemanagerapi.model.Status;

import java.time.LocalDateTime;

public class ActivityResponseBodyCreator {

  private static final LocalDateTime FIXED_DATE_TIME = LocalDateTime
      .of(2022, 8, 23, 10,40,21);

  public static ActivityResponseBody withIdAndName(long id, String name){
    return ActivityResponseBody.builder()
        .id(id)
        .name(name)
        .description("A simple project for my portfolio")
        .createdAt(FIXED_DATE_TIME)
        .status(Status.ACTIVE)
        .build();
  }

  public static ActivityResponseBody withIdNameAndDescription(long id, String name, String description){
    return ActivityResponseBody.builder()
        .id(id)
        .name(name)
        .description(description)
        .createdAt(FIXED_DATE_TIME)
        .status(Status.ACTIVE)
        .build();
  }

}
