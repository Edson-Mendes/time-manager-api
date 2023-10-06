package br.com.emendes.timemanagerapi.dto.request;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class ActivityRequest {

  @NotBlank(message = "name must not be null or blank")
  private String name;
  @NotBlank(message = "description must not be null or blank")
  private String description;

}
