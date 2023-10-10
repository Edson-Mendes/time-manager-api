package br.com.emendes.timemanagerapi.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Classe DTO para enviar informações de um Interval.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class IntervalResponse {

  private long id;
  private LocalDateTime startedAt;
  private LocalTime elapsedTime;

}
