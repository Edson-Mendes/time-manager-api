package br.com.emendes.timemanagerapi.repository;

import br.com.emendes.timemanagerapi.model.Activity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class ActivityRepository {


  public List<Activity> findAll() {
    Activity activity1 = Activity.builder()
        .id(1L)
        .name("Time Manager")
        .description("A simple project for manage your time with activities.")
        .createdAt(LocalDateTime.now())
        .enabled(true)
        .build();

    Activity activity2 = Activity.builder()
        .id(2L)
        .name("Finances API")
        .description("A simple project for financial control")
        .createdAt(LocalDateTime.now())
        .enabled(true)
        .build();

    return List.of(activity1, activity2);
//    return Collections.EMPTY_LIST;
  }
}
