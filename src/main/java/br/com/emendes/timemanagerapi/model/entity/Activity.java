package br.com.emendes.timemanagerapi.model.entity;

import br.com.emendes.timemanagerapi.dto.request.ActivityRequestBody;
import br.com.emendes.timemanagerapi.model.Status;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Classe que representa uma atividade do usuário (e.g. Curso de programação), possui:
 * <ul>
 *   <li>id - id da atividade no DB</li>
 *   <li>name - nome da atividade</li>
 *   <li>description - descrição da atividade</li>
 *   <li>createAt - data da criação da atividade</li>
 *   <li>enabled - boolean que indica se a atividade foi deletada</li>
 *   <li>concluded - boolean que indica se a atividade foi concluída</li>
 *   <li>intervals - lista de intervalos em que usuário trabalhou na atividade</li>
 * </ul>
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "tb_activity")
public class Activity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false)
  private String name;
  @Column(nullable = false)
  private String description;
  @Column(nullable = false)
  private LocalDateTime createdAt;
  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private Status status;
//  TODO: Ver se vale a pena mudar para SET
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "activity")
  private List<Interval> intervals;
  @ManyToOne(fetch = FetchType.LAZY)
  private User user;

  public void update(ActivityRequestBody activityRequestBody) {
    this.name = activityRequestBody.getName();
    this.description = activityRequestBody.getDescription();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Activity activity = (Activity) o;

    if (!Objects.equals(id, activity.id)) return false;
    if (!Objects.equals(name, activity.name)) return false;
    if (!Objects.equals(description, activity.description)) return false;
    if (!Objects.equals(createdAt, activity.createdAt)) return false;
    if (status != activity.status) return false;
    return Objects.equals(intervals, activity.intervals);
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (name != null ? name.hashCode() : 0);
    result = 31 * result + (description != null ? description.hashCode() : 0);
    result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
    result = 31 * result + (status != null ? status.hashCode() : 0);
    result = 31 * result + (intervals != null ? intervals.hashCode() : 0);
    return result;
  }
}
