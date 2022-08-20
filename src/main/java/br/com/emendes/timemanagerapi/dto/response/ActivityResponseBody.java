package br.com.emendes.timemanagerapi.dto.response;

import br.com.emendes.timemanagerapi.model.Activity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ActivityResponseBody {

  private Long id;
  private String name;
  private String description;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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
