package br.com.emendes.timemanagerapi.service;

import br.com.emendes.timemanagerapi.dto.request.ActivityRequestBody;
import br.com.emendes.timemanagerapi.dto.request.UpdateStatusRequest;
import br.com.emendes.timemanagerapi.dto.response.ActivityResponse;
import br.com.emendes.timemanagerapi.exception.ActivityNotFoundException;
import br.com.emendes.timemanagerapi.model.Status;
import br.com.emendes.timemanagerapi.model.entity.Activity;
import br.com.emendes.timemanagerapi.model.entity.User;
import br.com.emendes.timemanagerapi.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivityService {

  private final ActivityRepository activityRepository;

  public Page<ActivityResponse> find(Pageable pageable) {
    Page<Activity> activitiesPage = activityRepository.findByStatusIsNot(pageable, Status.DELETED);
    if (activitiesPage.getTotalElements() == 0) {
      throw new ActivityNotFoundException("Não possui atividades");
    }
    return activitiesPage.map(ActivityResponse::new);
  }

  public Activity findById(long id) {
    return activityRepository.findById(id).orElseThrow(
        () -> new ActivityNotFoundException("Activity not found for id: " + id));
  }

  public ActivityResponse create(ActivityRequestBody activityRequestBody) {
    User user = getCurrentUser();
    Activity activitySaved = activityRepository.save(activityRequestBody.toActivity(user));

    return new ActivityResponse(activitySaved);
  }


  public void update(long id, ActivityRequestBody activityRequestBody) {
    Activity activityToBeUpdated = findByIdAndNotDeleted(id);
    activityToBeUpdated.update(activityRequestBody);
    activityRepository.save(activityToBeUpdated);
  }

  public void deleteActivityById(long id) {
    updateStatusById(id, Status.DELETED);
  }

  public void updateStatusById(long id, UpdateStatusRequest updateStatusRequest) {
    updateStatusById(id, updateStatusRequest.toStatus());
  }

  private void updateStatusById(long id, Status status) {
    Activity activityToChangeStatus = findByIdAndNotDeleted(id);
    activityToChangeStatus.setStatus(status);
    activityRepository.save(activityToChangeStatus);
  }

  private Activity findByIdAndNotDeleted(long id) {
    Activity activityFound = findById(id);
    if (activityFound.getStatus() == Status.DELETED) {
      throw new ActivityNotFoundException("Activity not found for id: " + id);
    }
    return activityFound;
  }
  private User getCurrentUser() {
    return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }

}
