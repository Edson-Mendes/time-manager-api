package br.com.emendes.timemanagerapi.util.creator;

import br.com.emendes.timemanagerapi.model.Activity;

import java.time.LocalDateTime;

public class ActivityCreator {

  public static Activity activityWithIdAndName(Long id, String name){
    return Activity.builder()
        .id(id)
        .name(name)
        .description("A simple project for my portfolio")
        .createdAt(LocalDateTime.now())
        .enabled(true)
        .build();
  }

  public static Activity activityWithoutIdAndWithNameAndDescription(String name, String description){
    return Activity.builder()
        .name(name)
        .description(description)
        .createdAt(LocalDateTime.now())
        .enabled(true)
        .build();
  }

  public static Activity activityForTests(){
    return Activity.builder()
        .id(1L)
        .name("Lorem Ipsum Activity")
        .description("A simple project for my portfolio")
        .createdAt(LocalDateTime.now())
        .enabled(true)
        .build();
  }

}
