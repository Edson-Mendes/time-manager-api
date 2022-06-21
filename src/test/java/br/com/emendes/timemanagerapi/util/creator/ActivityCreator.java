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

}
