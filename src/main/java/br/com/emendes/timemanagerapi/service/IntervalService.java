package br.com.emendes.timemanagerapi.service;

import br.com.emendes.timemanagerapi.dto.request.IntervalRequestBody;
import br.com.emendes.timemanagerapi.dto.response.IntervalResponseBody;
import br.com.emendes.timemanagerapi.model.Activity;
import br.com.emendes.timemanagerapi.model.Interval;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class IntervalService {

  public IntervalResponseBody create(long activityId, IntervalRequestBody requestBody){
    Interval interval = Interval.builder()
        .id(100L)
        .startedAt(requestBody.getStartedAt())
        .elapsedTime(requestBody.getElapsedTime())
        .activity(Activity.builder().id(activityId).build())
        .build();
    return new IntervalResponseBody(interval);
  }

}
