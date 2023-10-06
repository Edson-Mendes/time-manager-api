package br.com.emendes.timemanagerapi.service.impl;

import br.com.emendes.timemanagerapi.dto.request.ActivityRequest;
import br.com.emendes.timemanagerapi.dto.request.UpdateStatusRequest;
import br.com.emendes.timemanagerapi.dto.response.ActivityResponse;
import br.com.emendes.timemanagerapi.exception.ActivityNotFoundException;
import br.com.emendes.timemanagerapi.mapper.ActivityMapper;
import br.com.emendes.timemanagerapi.model.Status;
import br.com.emendes.timemanagerapi.model.entity.Activity;
import br.com.emendes.timemanagerapi.model.entity.User;
import br.com.emendes.timemanagerapi.repository.ActivityRepository;
import br.com.emendes.timemanagerapi.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Implementação de {@link ActivityService}.
 */
@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {

  private final ActivityRepository activityRepository;
  private final ActivityMapper activityMapper;

  @Override
  public Page<ActivityResponse> find(Pageable pageable) {
    User currentUser = getCurrentUser();
    Page<Activity> activitiesPage = activityRepository.findByUserAndStatusIsNot(pageable, currentUser, Status.DELETED);
    return activitiesPage.map(activityMapper::toActivityResponse);
  }

  /**
   * @throws ActivityNotFoundException se o usuário não possuir activity com dado {@code id}.
   */
  @Override
  public Activity findById(long id) {
    User currentUser = getCurrentUser();
    return activityRepository.findByIdAndUser(id, currentUser).orElseThrow(
        () -> new ActivityNotFoundException("Activity not found for id: " + id));
  }

  @Override
  public ActivityResponse create(ActivityRequest activityRequest) {
    User user = getCurrentUser();
    Activity activity = activityMapper.toActivity(activityRequest);

    activity.setCreatedAt(LocalDateTime.now());
    activity.setStatus(Status.ACTIVE);
    activity.setUser(user);

    activityRepository.save(activity);

    return activityMapper.toActivityResponse(activity);
  }

  @Override
  public void update(long id, ActivityRequest activityRequest) {
    Activity activityToBeUpdated = findByIdAndNotDeleted(id);

    activityMapper.merge(activityRequest, activityToBeUpdated);
    activityRepository.save(activityToBeUpdated);
  }

  @Override
  public void deleteActivityById(long id) {
    updateStatusById(id, Status.DELETED);
  }

  @Override
  public void updateStatusById(long id, UpdateStatusRequest updateStatusRequest) {
    updateStatusById(id, updateStatusRequest.toStatus());
  }

  /**
   * Atualiza o status de uma Activity por id.
   *
   * @param id     identificador da Activity.
   * @param status novo {@link Status} da activity.
   */
  private void updateStatusById(long id, Status status) {
    Activity activityToChangeStatus = findByIdAndNotDeleted(id);
    activityToChangeStatus.setStatus(status);
    activityRepository.save(activityToChangeStatus);
  }

  /**
   * Busca Activity por id do usuário logado e que não esteja com status DELETED.
   *
   * @param id da Activity a ser encontrada.
   * @return Activity encontrada.
   * @throws ActivityNotFoundException se a Activity estiver com status DELETED.
   */
  private Activity findByIdAndNotDeleted(long id) {
    Activity activityFound = findById(id);
    if (activityFound.getStatus() == Status.DELETED) {
      throw new ActivityNotFoundException("Activity not found for id: " + id);
    }
    return activityFound;
  }

  /**
   * Retorna o usuário atual.
   */
  private User getCurrentUser() {
    return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }

}
