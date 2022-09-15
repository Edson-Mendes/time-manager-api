package br.com.emendes.timemanagerapi.model;

import br.com.emendes.timemanagerapi.dto.request.ActivityRequestBody;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

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
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
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
  private boolean enabled;
  @Column(nullable = false)
  private boolean concluded;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "activity")
  private List<Interval> intervals;

  public void update(ActivityRequestBody activityRequestBody) {
    this.name = activityRequestBody.getName();
    this.description = activityRequestBody.getDescription();
  }
}
