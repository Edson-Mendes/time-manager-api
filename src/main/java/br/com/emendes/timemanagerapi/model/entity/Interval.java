package br.com.emendes.timemanagerapi.model.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

/**
 * Classe que representa um <em>intervalo</em> de tempo de execução/trabalho em uma atividade.
 * <p>Possui <strong>data e hora</strong> em que começou e quanto <strong>tempo</strong> durou.</p>
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity(name = "tb_interval")
public class Interval {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false)
  private LocalDateTime startedAt;
  @Column(nullable = false)
  private LocalTime elapsedTime;
  @ManyToOne()
  private Activity activity;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Interval interval = (Interval) o;

    if (!Objects.equals(id, interval.id)) return false;
    if (!Objects.equals(startedAt, interval.startedAt)) return false;
    if (!Objects.equals(elapsedTime, interval.elapsedTime)) return false;
    return Objects.equals(activity, interval.activity);
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (startedAt != null ? startedAt.hashCode() : 0);
    result = 31 * result + (elapsedTime != null ? elapsedTime.hashCode() : 0);
    result = 31 * result + (activity != null ? activity.hashCode() : 0);
    return result;
  }
}
