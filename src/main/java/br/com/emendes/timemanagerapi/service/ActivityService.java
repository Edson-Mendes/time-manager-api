package br.com.emendes.timemanagerapi.service;

import br.com.emendes.timemanagerapi.dto.request.ActivityRequestBody;
import br.com.emendes.timemanagerapi.dto.response.ActivityResponseBody;
import br.com.emendes.timemanagerapi.exception.ActivitiesNotFoundException;
import br.com.emendes.timemanagerapi.model.Activity;
import br.com.emendes.timemanagerapi.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityService {

  private final ActivityRepository activityRepository;

  public List<ActivityResponseBody> findAll() {
    List<Activity> activities = activityRepository.findAll();
    if(activities.isEmpty()){
      throw new ActivitiesNotFoundException("Não possui atividades");
    }

    return activities.stream().map(ActivityResponseBody::new).toList();
  }

  public ActivityResponseBody create(ActivityRequestBody activityRequestBody) {
    Activity activitySaved = activityRepository.save(activityRequestBody.toActivity());

    return new ActivityResponseBody(activitySaved);
  }
}
