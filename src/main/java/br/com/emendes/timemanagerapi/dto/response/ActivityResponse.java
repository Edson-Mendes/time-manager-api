package br.com.emendes.timemanagerapi.dto.response;

import br.com.emendes.timemanagerapi.model.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class ActivityResponse {

  private long id;
  private String name;
  private String description;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime createdAt;
  private Status status;

}
