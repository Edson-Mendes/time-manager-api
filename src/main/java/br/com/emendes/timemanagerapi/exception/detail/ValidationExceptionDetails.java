package br.com.emendes.timemanagerapi.exception.detail;

import br.com.emendes.timemanagerapi.exception.detail.ExceptionDetails;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class ValidationExceptionDetails extends ExceptionDetails {

  private String fields;
  private String messages;

}
