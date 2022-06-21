package br.com.emendes.timemanagerapi.dto.request;

import br.com.emendes.timemanagerapi.model.Activity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ActivityRequestBody {

  private String name;
  private String description;

  public Activity toActivity() {
    return Activity.builder()
        .name(this.name)
        .description(this.description)
        .createdAt(LocalDateTime.now())
        .enabled(true)
        .build();
  }
}
