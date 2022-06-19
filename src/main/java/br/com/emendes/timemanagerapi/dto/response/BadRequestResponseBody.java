package br.com.emendes.timemanagerapi.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BadRequestResponseBody {

  private String title;
  private int status;
  private String details;
  private LocalDateTime timestamp;

}
