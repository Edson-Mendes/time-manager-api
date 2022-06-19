package br.com.emendes.timemanagerapi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Activity {

  private Long id;
  private String name;
  private String description;
  private LocalDateTime createdAt;
  private boolean enabled;

}
