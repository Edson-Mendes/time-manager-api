package br.com.emendes.timemanagerapi.util.creator;

import br.com.emendes.timemanagerapi.dto.response.ActivityResponseBody;

import java.time.LocalDateTime;

public class ActivityResponseBodyCreator {

  private static final LocalDateTime FIXED_DATE_TIME = LocalDateTime
      .of(2022, 8, 23, 10,40,21);

  public static ActivityResponseBody withIdAndName(long id, String name){
    return new ActivityResponseBody(
        id, name, "A simple project for my portfolio", FIXED_DATE_TIME, true);
  }

  public static ActivityResponseBody withIdNameAndDescription(long id, String name, String description){
    return new ActivityResponseBody(
        id, name, description, FIXED_DATE_TIME, true);
  }

}
