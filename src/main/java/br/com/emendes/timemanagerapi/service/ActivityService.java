package br.com.emendes.timemanagerapi.service;

import br.com.emendes.timemanagerapi.dto.request.ActivityRequestBody;
import br.com.emendes.timemanagerapi.dto.response.ActivityResponseBody;
import br.com.emendes.timemanagerapi.exception.ActivityNotFoundException;
import br.com.emendes.timemanagerapi.model.Activity;
import br.com.emendes.timemanagerapi.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityService {

  private final ActivityRepository activityRepository;

  public List<ActivityResponseBody> findAll() {
    List<Activity> activities = activityRepository.findAll();
    if (activities.isEmpty()) {
      throw new ActivityNotFoundException("Não possui atividades");
    }

    return activities.stream().map(ActivityResponseBody::new).toList();
  }

  private Activity findById(long id) {
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
}
