package br.com.emendes.timemanagerapi.mapper.impl;

import br.com.emendes.timemanagerapi.dto.request.IntervalRequest;
import br.com.emendes.timemanagerapi.dto.response.IntervalResponse;
import br.com.emendes.timemanagerapi.mapper.IntervalMapper;
import br.com.emendes.timemanagerapi.model.entity.Interval;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Implementação de {@link IntervalMapper}.
 */
@Component
public class IntervalMapperImpl implements IntervalMapper {

  /**
   * @throws IllegalArgumentException se activityRequest for null.
   */
  @Override
  public Interval toInterval(IntervalRequest intervalRequest) {
    Assert.notNull(intervalRequest, "intervalRequest must not be null");

    return Interval.builder()
        .startedAt(LocalDateTime.parse(intervalRequest.getStartedAt()))
        .elapsedTime(LocalTime.parse(intervalRequest.getElapsedTime()))
        .build();
  }

  @Override
  public IntervalResponse toIntervalResponse(Interval interval) {
    Assert.notNull(interval, "interval must not be null");

    return IntervalResponse.builder()
        .id(interval.getId())
        .startedAt(interval.getStartedAt())
        .elapsedTime(interval.getElapsedTime())
        .build();
  }

}
