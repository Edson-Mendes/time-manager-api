package br.com.emendes.timemanagerapi.service;

import br.com.emendes.timemanagerapi.exception.ActivitiesNotFoundException;
import br.com.emendes.timemanagerapi.model.Activity;
import br.com.emendes.timemanagerapi.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityService {

  private final ActivityRepository activityRepository;

  public List<Activity> findAll() {
    List<Activity> activities = activityRepository.findAll();
    if(activities.isEmpty()){
      throw new ActivitiesNotFoundException("Não possui atividades");
    }

    return activities;
  }
}
