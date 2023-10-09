package br.com.emendes.timemanagerapi.mapper;

import br.com.emendes.timemanagerapi.dto.request.IntervalRequest;
import br.com.emendes.timemanagerapi.dto.response.IntervalResponse;
import br.com.emendes.timemanagerapi.model.entity.Interval;

/**
 * Interface component que contém as abstrações de mapeamento de DTOs para a entidade Interval e vice-versa.
 */
public interface IntervalMapper {

  /**
   * Mapeia o DTO IntervalRequest para a entidade Interval.
   *
   * @param intervalRequest que deve ser mapeado para Interval
   * @return Interval com dados vindo de intervalRequest.
   */
  Interval toInterval(IntervalRequest intervalRequest);

  /**
   * Mapeia a entidade Interval para o DTO IntervalResponse.
   *
   * @param interval que deve ser mapeado para IntervalResponse.
   * @return IntervalResponse com dados vindo de Interval.
   */
  IntervalResponse toIntervalResponse(Interval interval);

}
