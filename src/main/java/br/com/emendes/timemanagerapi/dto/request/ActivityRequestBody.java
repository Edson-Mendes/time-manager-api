package br.com.emendes.timemanagerapi.dto.request;

import br.com.emendes.timemanagerapi.model.Activity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ActivityRequestBody {

  @NotBlank(message = "name must not be null or blank")
  private String name;
  @NotBlank(message = "description must not be null or blank")
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
