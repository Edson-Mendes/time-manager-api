package br.com.emendes.timemanagerapi.service;

import br.com.emendes.timemanagerapi.dto.request.IntervalRequest;
import br.com.emendes.timemanagerapi.dto.response.IntervalResponseBody;
import br.com.emendes.timemanagerapi.exception.IntervalNotFoundException;
import br.com.emendes.timemanagerapi.model.entity.Interval;
import br.com.emendes.timemanagerapi.repository.IntervalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class IntervalService {

  private final ActivityService activityService;
  private final IntervalRepository intervalRepository;

  public IntervalResponseBody create(long activityId, IntervalRequest requestBody){
    Interval intervalToBeSaved = requestBody.toInterval(activityService.findById(activityId));
    Interval intervalSaved = intervalRepository.save(intervalToBeSaved);

    return new IntervalResponseBody(intervalSaved);
  }

  public Page<IntervalResponseBody> find(long activityId, Pageable pageable) {
    Page<Interval> intervalPage = intervalRepository.findByActivity(
        activityService.findById(activityId), pageable);
    return intervalPage.map(IntervalResponseBody::new);
  }

  //  TODO: Pensar em por as três queries abaixo em uma única query.
  public void delete(long activityId, long intervalId) {
    activityService.findById(activityId);
    intervalRepository.delete(findById(intervalId));
  }

  private Interval findById(long id){
    return intervalRepository.findById(id).orElseThrow(
        () -> new IntervalNotFoundException("Interval not found for id: " + id));
  }

}
