package br.com.emendes.timemanagerapi.dto.request;

import br.com.emendes.timemanagerapi.model.Activity;
import br.com.emendes.timemanagerapi.model.Interval;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class IntervalRequestBody {

  private LocalDateTime startedAt;
  private LocalTime elapsedTime;

  public Interval toInterval(Activity activity){
    return Interval.builder()
        .activity(activity)
        .startedAt(startedAt)
        .elapsedTime(elapsedTime)
        .build();
  }
}
