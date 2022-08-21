package br.com.emendes.timemanagerapi.service;

import br.com.emendes.timemanagerapi.dto.request.IntervalRequestBody;
import br.com.emendes.timemanagerapi.dto.response.IntervalResponseBody;
import br.com.emendes.timemanagerapi.model.Interval;
import br.com.emendes.timemanagerapi.repository.IntervalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class IntervalService {

  private final ActivityService activityService;

  private final IntervalRepository intervalRepository;

  public IntervalResponseBody create(long activityId, IntervalRequestBody requestBody){
    Interval intervalToBeSaved = requestBody.toInterval(activityService.findById(activityId));
    Interval intervalSaved = intervalRepository.save(intervalToBeSaved);

    return new IntervalResponseBody(intervalSaved);
  }

}
