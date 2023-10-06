package br.com.emendes.timemanagerapi.util.creator;

import br.com.emendes.timemanagerapi.dto.response.ActivityResponse;
import br.com.emendes.timemanagerapi.model.Status;

import java.time.LocalDateTime;

public class ActivityResponseCreator {

  private static final LocalDateTime FIXED_DATE_TIME = LocalDateTime
      .of(2022, 8, 23, 10,40,21);

  public static ActivityResponse withIdAndName(long id, String name){
    return ActivityResponse.builder()
        .id(id)
        .name(name)
        .description("A simple project for my portfolio")
        .createdAt(FIXED_DATE_TIME)
        .status(Status.ACTIVE)
        .build();
  }

  public static ActivityResponse withIdNameAndDescription(long id, String name, String description){
    return ActivityResponse.builder()
        .id(id)
        .name(name)
        .description(description)
        .createdAt(FIXED_DATE_TIME)
        .status(Status.ACTIVE)
        .build();
  }

}
