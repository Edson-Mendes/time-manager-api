package br.com.emendes.timemanagerapi.dto.request;

import br.com.emendes.timemanagerapi.model.Activity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Data
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Log4j2
public class ActivityRequestBody {

  @NotBlank(message = "name must not be null or blank")
  private String name;
  @NotBlank(message = "description must not be null or blank")
  private String description;

  public Activity toActivity() {
    return Activity.builder()
        .name(this.name)
        .description(this.description)
        .createdAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
        .enabled(true)
        .concluded(false)
        .build();
  }
}
