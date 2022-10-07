package br.com.emendes.timemanagerapi.dto.response;

import br.com.emendes.timemanagerapi.model.entity.Interval;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class IntervalResponseBody {

  private long id;
  private LocalDateTime startedAt;
  private LocalTime elapsedTime;

  public IntervalResponseBody(Interval interval){
    this.id = interval.getId();
    this.startedAt = interval.getStartedAt();
    this.elapsedTime = interval.getElapsedTime();
  }

}
