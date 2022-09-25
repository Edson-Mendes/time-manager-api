package br.com.emendes.timemanagerapi.dto.request;

import br.com.emendes.timemanagerapi.model.Status;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class UpdateStatusRequest {

  @NotBlank(message = "status must not be null or blank")
  private String status;

  public Status toStatus(){
    return Status.valueOf(this.status.toUpperCase());
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    UpdateStatusRequest that = (UpdateStatusRequest) o;

    return status != null ? status.equalsIgnoreCase(that.status) : that.status == null;
  }

  @Override
  public int hashCode() {
    return status != null ? status.hashCode() : 0;
  }
}
