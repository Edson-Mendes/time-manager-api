package br.com.emendes.timemanagerapi.dto.request;

import br.com.emendes.timemanagerapi.model.entity.Activity;
import br.com.emendes.timemanagerapi.model.entity.Interval;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class IntervalRequest {

  @NotBlank(message = "startedAt must not be null or blank")
  private String startedAt;
  @NotBlank(message = "elapsedTime must not be null or blank")
  private String elapsedTime;

  public Interval toInterval(Activity activity){
    return Interval.builder()
        .activity(activity)
        .startedAt(LocalDateTime.parse(startedAt))
        .elapsedTime(LocalTime.parse(elapsedTime))
        .build();
  }
}
