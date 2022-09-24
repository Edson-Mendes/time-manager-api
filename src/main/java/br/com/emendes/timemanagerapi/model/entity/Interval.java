package br.com.emendes.timemanagerapi.model.entity;

import br.com.emendes.timemanagerapi.model.entity.Activity;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Classe que representa um <em>intervalo</em> de tempo de execução/trabalho em uma atividade.
 * <p>Possui <strong>data e hora</strong> em que começou e quanto <strong>tempo</strong> durou.</p>
 */
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@Data
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

}
