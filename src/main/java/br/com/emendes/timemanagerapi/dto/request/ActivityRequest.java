package br.com.emendes.timemanagerapi.dto.request;

import br.com.emendes.timemanagerapi.model.entity.Activity;
import br.com.emendes.timemanagerapi.model.Status;
import br.com.emendes.timemanagerapi.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Data
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ActivityRequest {

  @NotBlank(message = "name must not be null or blank")
  private String name;
  @NotBlank(message = "description must not be null or blank")
  private String description;

  public Activity toActivity(User user) {
    return Activity.builder()
        .name(this.name)
        .description(this.description)
        .createdAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
        .status(Status.ACTIVE)
        .user(user)
        .build();
  }
}
