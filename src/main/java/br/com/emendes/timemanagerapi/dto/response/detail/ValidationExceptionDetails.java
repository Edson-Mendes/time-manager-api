package br.com.emendes.timemanagerapi.dto.response.detail;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
public class ValidationExceptionDetails extends ExceptionDetails {

  private String fields;
  private String messages;

}
