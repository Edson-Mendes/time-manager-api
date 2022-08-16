package br.com.emendes.timemanagerapi.dto.request;

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

}
