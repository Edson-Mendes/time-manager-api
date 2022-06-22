package br.com.emendes.timemanagerapi.model;

import br.com.emendes.timemanagerapi.dto.request.ActivityRequestBody;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "tb_activity")
public class Activity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  private String description;
  private LocalDateTime createdAt;
  private boolean enabled;

  public void update(ActivityRequestBody activityRequestBody) {
    this.name = activityRequestBody.getName();
    this.description = activityRequestBody.getDescription();
  }
}
