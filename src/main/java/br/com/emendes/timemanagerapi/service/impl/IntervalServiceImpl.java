package br.com.emendes.timemanagerapi.service.impl;

import br.com.emendes.timemanagerapi.dto.request.IntervalRequest;
import br.com.emendes.timemanagerapi.dto.response.IntervalResponse;
import br.com.emendes.timemanagerapi.exception.ActivityNotFoundException;
import br.com.emendes.timemanagerapi.exception.IntervalCreationException;
import br.com.emendes.timemanagerapi.exception.IntervalNotFoundException;
import br.com.emendes.timemanagerapi.mapper.IntervalMapper;
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

/**
 * Implementação de {@link IntervalService}.
 */
@RequiredArgsConstructor
@Service
public class IntervalServiceImpl implements IntervalService {

  private final ActivityService activityService;
  private final IntervalRepository intervalRepository;
  private final IntervalMapper intervalMapper;

  /**
   * @throws IntervalCreationException caso a Activity esteja com status diferente de Status.ACTIVE.
   */
  @Override
  public IntervalResponse create(long activityId, IntervalRequest intervalRequest) {
    Activity activity = activityService.findById(activityId);
    if (activity.getStatus() != Status.ACTIVE) {
      throw new IntervalCreationException("Cannot create interval on non active activity");
    }
    Interval interval = intervalMapper.toInterval(intervalRequest);
    interval.setActivity(activity);

    intervalRepository.save(interval);

    return intervalMapper.toIntervalResponse(interval);
  }

  /**
   * @throws ActivityNotFoundException caso a Activity esteja com status igual Status.DELETED.
   */
  @Override
  public Page<IntervalResponse> find(long activityId, Pageable pageable) {
    Activity activity = activityService.findById(activityId);
    if (activity.getStatus() == Status.DELETED) {
      throw new ActivityNotFoundException("Activity not found for id: " + activityId);
    }
    Page<Interval> intervalPage = intervalRepository.findByActivity(
        activity, pageable);
    return intervalPage.map(intervalMapper::toIntervalResponse);
  }

  //  TODO: Pensar em por as três queries abaixo em uma única query.
  @Override
  public void delete(long activityId, long intervalId) {
    activityService.findById(activityId);
    intervalRepository.delete(findById(intervalId));
  }

  /**
   * Busca um Interval por id.
   *
   * @param id identificador do Interval.
   * @return Interval para o dado id.
   * @throws IntervalNotFoundException caso não exista interval com o dado id.
   */
  private Interval findById(long id) {
    return intervalRepository.findById(id).orElseThrow(
        () -> new IntervalNotFoundException("Interval not found for id: " + id));
  }

}
