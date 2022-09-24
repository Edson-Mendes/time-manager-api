package br.com.emendes.timemanagerapi.service;

import br.com.emendes.timemanagerapi.dto.request.ActivityRequestBody;
import br.com.emendes.timemanagerapi.dto.request.UpdateStatusRequest;
import br.com.emendes.timemanagerapi.dto.response.ActivityResponseBody;
import br.com.emendes.timemanagerapi.exception.ActivityNotFoundException;
import br.com.emendes.timemanagerapi.model.entity.Activity;
import br.com.emendes.timemanagerapi.model.Status;
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
    Page<Activity> activitiesPage = activityRepository.findByStatusIsNot(pageable, Status.DELETED);
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
//    TODO: Não é usado na aplicação, buscar e deletar todos os testes.
    activityRepository.delete(findById(id));
  }

  public void deleteActivityById(long id){
    updateStatusById(id, Status.DELETED);
  }

  public void concludeActivityById(long id) {
//    TODO: Não é usado na aplicação, buscar e deletar todos os testes.
    updateStatusById(id, Status.CONCLUDED);
  }

  public void updateStatusById(long id, UpdateStatusRequest updateStatusRequest){
    updateStatusById(id, updateStatusRequest.toStatus());
  }

  private void updateStatusById(long id, Status status){
    Activity activityToChangeStatus = findById(id);
    activityToChangeStatus.setStatus(status);
    activityRepository.save(activityToChangeStatus);
  }
}
