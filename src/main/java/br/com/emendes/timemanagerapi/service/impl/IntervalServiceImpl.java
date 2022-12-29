package br.com.emendes.timemanagerapi.service.impl;

import br.com.emendes.timemanagerapi.dto.request.IntervalRequest;
import br.com.emendes.timemanagerapi.dto.response.IntervalResponse;
import br.com.emendes.timemanagerapi.exception.ActivityNotFoundException;
import br.com.emendes.timemanagerapi.exception.IntervalCreationException;
import br.com.emendes.timemanagerapi.exception.IntervalNotFoundException;
import br.com.emendes.timemanagerapi.model.Status;
import br.com.emendes.timemanagerapi.model.entity.Activity;
import br.com.emendes.timemanagerapi.model.entity.Interval;
import br.com.emendes.timemanagerapi.repository.IntervalRepository;
import br.com.emendes.timemanagerapi.service.ActivityService;
import br.com.emendes.timemanagerapi.service.IntervalService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class IntervalServiceImpl implements IntervalService {

  private final ActivityService activityService;
  private final IntervalRepository intervalRepository;

  @Override
  public IntervalResponse create(long activityId, IntervalRequest requestBody) {
    Activity activity = activityService.findById(activityId);
    if(activity.getStatus() != Status.ACTIVE){
      throw new IntervalCreationException("Cannot create interval on non active activity");
    }
    Interval intervalToBeSaved = requestBody.toInterval(activity);
    Interval intervalSaved = intervalRepository.save(intervalToBeSaved);

    return new IntervalResponse(intervalSaved);
  }

  @Override
  public Page<IntervalResponse> find(long activityId, Pageable pageable) {
    Activity activity = activityService.findById(activityId);
    if(activity.getStatus() == Status.DELETED){
      throw new ActivityNotFoundException("Activity not found for id: " + activityId);
    }
    Page<Interval> intervalPage = intervalRepository.findByActivity(
        activity, pageable);
    return intervalPage.map(IntervalResponse::new);
  }

  //  TODO: Pensar em por as três queries abaixo em uma única query.
  @Override
  public void delete(long activityId, long intervalId) {
    activityService.findById(activityId);
    intervalRepository.delete(findById(intervalId));
  }

  private Interval findById(long id) {
    return intervalRepository.findById(id).orElseThrow(
        () -> new IntervalNotFoundException("Interval not found for id: " + id));
  }

}
