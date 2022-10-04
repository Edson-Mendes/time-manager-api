package br.com.emendes.timemanagerapi.util.creator;

import br.com.emendes.timemanagerapi.model.entity.Activity;
import br.com.emendes.timemanagerapi.model.entity.Interval;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class IntervalCreator {

  public static Interval withActivityIdStartedAtAndElapsedTime(long activityId, String startedAt, String elapsedTime) {
    return Interval.builder()
        .activity(ActivityCreator.withIdAndName(activityId, "Lorem Ipsum Activity"))
        .startedAt(LocalDateTime.parse(startedAt))
        .elapsedTime(LocalTime.parse(elapsedTime))
        .build();
  }

  public static Interval withActivityStartedAtAndElapsedTime(Activity activity, String startedAt, String elapsedTime) {
    return Interval.builder()
        .activity(activity)
        .startedAt(LocalDateTime.parse(startedAt))
        .elapsedTime(LocalTime.parse(elapsedTime))
        .build();
  }

  public static Interval withIdActivityIdStartedAtAndElapsedTime(long id, long activityId, String startedAt, String elapsedTime) {
    return Interval.builder()
        .id(id)
        .activity(ActivityCreator.withIdAndName(activityId, "Lorem Ipsum Activity"))
        .startedAt(LocalDateTime.parse(startedAt))
        .elapsedTime(LocalTime.parse(elapsedTime))
        .build();
  }
}
