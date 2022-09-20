package br.com.emendes.timemanagerapi.service;

import br.com.emendes.timemanagerapi.dto.request.ActivityRequestBody;
import br.com.emendes.timemanagerapi.dto.response.ActivityResponseBody;
import br.com.emendes.timemanagerapi.exception.ActivityNotFoundException;
import br.com.emendes.timemanagerapi.model.Activity;
import br.com.emendes.timemanagerapi.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivityService {

  private final ActivityRepository activityRepository;

  public Page<ActivityResponseBody> find(Pageable pageable) {
    Page<Activity> activitiesPage = activityRepository.findByEnabled(pageable, true);
    if (activitiesPage.getTotalElements() == 0) {
      throw new ActivityNotFoundException("Não possui atividades");
    }
    return activitiesPage.map(ActivityResponseBody::new);
  }

  public Activity findById(long id) {
    return activityRepository.findById(id).orElseThrow(
        () -> new ActivityNotFoundException("Activity not found for id: " + id));
  }

  public ActivityResponseBody create(ActivityRequestBody activityRequestBody) {
    Activity activitySaved = activityRepository.save(activityRequestBody.toActivity());

    return new ActivityResponseBody(activitySaved);
  }

  public void update(long id, ActivityRequestBody activityRequestBody) {
    Activity activityToBeUpdated = findById(id);
    activityToBeUpdated.update(activityRequestBody);
    activityRepository.save(activityToBeUpdated);
  }

  public void deleteById(long id) {
    activityRepository.delete(findById(id));
  }
  
  public void disableActivityById(long id){
    Activity activityToDisable = findById(id);
    activityToDisable.setEnabled(false);
    activityRepository.save(activityToDisable);
  }

  public void concludeActivityById(long id, boolean status) {
    Activity activityToSwitchConcluded = findById(id);
    activityToSwitchConcluded.setConcluded(status);
    activityRepository.save(activityToSwitchConcluded);
  }
}
