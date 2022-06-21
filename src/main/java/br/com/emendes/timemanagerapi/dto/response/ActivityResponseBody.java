package br.com.emendes.timemanagerapi.dto.response;

import br.com.emendes.timemanagerapi.model.Activity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ActivityResponseBody {

  private Long id;
  private String name;
  private String description;
  private LocalDateTime createdAt;
  private boolean enabled;

  public ActivityResponseBody(Activity activity){
    this.id = activity.getId();
    this.name = activity.getName();
    this.description = activity.getDescription();
    this.createdAt = activity.getCreatedAt();
    this.enabled = activity.isEnabled();
  }

}
