package br.com.emendes.timemanagerapi.dto.request;

import br.com.emendes.timemanagerapi.model.entity.Activity;
import br.com.emendes.timemanagerapi.model.entity.Interval;
import br.com.emendes.timemanagerapi.validation.anotation.DateTimeValidation;
import br.com.emendes.timemanagerapi.validation.anotation.TimeValidation;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class IntervalRequest {

  @NotBlank(message = "startedAt must not be null or blank")
  @DateTimeValidation
  private String startedAt;
  @NotBlank(message = "elapsedTime must not be null or blank")
  @TimeValidation
  private String elapsedTime;

  public Interval toInterval(Activity activity){
    return Interval.builder()
        .activity(activity)
        .startedAt(LocalDateTime.parse(startedAt))
        .elapsedTime(LocalTime.parse(elapsedTime))
        .build();
  }
}
