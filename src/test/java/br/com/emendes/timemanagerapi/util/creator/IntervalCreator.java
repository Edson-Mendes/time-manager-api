package br.com.emendes.timemanagerapi.util.creator;

import br.com.emendes.timemanagerapi.model.entity.Activity;
import br.com.emendes.timemanagerapi.model.entity.Interval;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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

  public static List<Interval> intervals() {
    List<Interval> intervals = new ArrayList<>();

    Interval firstInterval = Interval.builder()
        .id(1_000_000L)
        .startedAt(LocalDateTime.parse("2023-10-06T15:00:00"))
        .elapsedTime(LocalTime.parse("00:30:00"))
        .build();
    Interval secondInterval = Interval.builder()
        .id(1_000_001L)
        .startedAt(LocalDateTime.parse("2023-10-06T22:00:00"))
        .elapsedTime(LocalTime.parse("00:45:00"))
        .build();

    intervals.add(firstInterval);
    intervals.add(secondInterval);

    return intervals;
  }

}
