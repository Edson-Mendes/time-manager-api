package br.com.emendes.timemanagerapi.dto.request;

import br.com.emendes.timemanagerapi.validation.anotation.DateTimeValidation;
import br.com.emendes.timemanagerapi.validation.anotation.TimeValidation;
import lombok.*;

import javax.validation.constraints.NotBlank;

/**
 * Classe DTO para receber dados de um Interval.
 */
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

}
