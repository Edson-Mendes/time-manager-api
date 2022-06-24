package br.com.emendes.timemanagerapi.exception.detail;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
public class ExceptionDetails {

  private String title;
  private int status;
  private String details;
  private LocalDateTime timestamp;

}
