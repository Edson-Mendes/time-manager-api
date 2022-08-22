package br.com.emendes.timemanagerapi.util.creator;

import br.com.emendes.timemanagerapi.model.Interval;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class IntervalCreator {
  public static Interval intervalToBeSavedTest() {
    return Interval.builder()
        .activity(ActivityCreator.activityForTests())
        .startedAt(LocalDateTime.of(2022, 8, 16, 15, 7, 0))
        .elapsedTime(LocalTime.of(0,30,0))
        .build();
  }

  public static Interval intervalSavedTest() {
    return Interval.builder()
        .id(100L)
        .startedAt(LocalDateTime.of(2022, 8, 16, 15, 7, 0))
        .elapsedTime(LocalTime.of(0,30,0))
        .activity(ActivityCreator.activityForTests())
        .build();
  }
}
