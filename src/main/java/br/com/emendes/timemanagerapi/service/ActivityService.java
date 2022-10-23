package br.com.emendes.timemanagerapi.service;

import br.com.emendes.timemanagerapi.dto.request.ActivityRequest;
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

  /**
   * Busca paginada de Activity do usuário logado.
   *
   * @param pageable informações da paginação
   * @return Page - ActivityResponse é um dto de Activity
   */
  public Page<ActivityResponse> find(Pageable pageable) {
    User currentUser = getCurrentUser();
    Page<Activity> activitiesPage = activityRepository.findByUserAndStatusIsNot(pageable, currentUser, Status.DELETED);
    return activitiesPage.map(ActivityResponse::new);
  }

  /**
   * Busca Activity por id do usuário logado.
   *
   * @param id da Activity desejada.
   * @return A Activity buscada.
   * @throws ActivityNotFoundException se o usuário não possuir activity com dado {@code id}.
   */
  public Activity findById(long id) {
    User currentUser = getCurrentUser();
    return activityRepository.findByIdAndUser(id, currentUser).orElseThrow(
        () -> new ActivityNotFoundException("Activity not found for id: " + id));
  }

  /**
   * Cria uma Activity relacionada ao usuário logado.
   *
   * @param activityRequest dados da Activity a ser salva.
   * @return ActivityResponse - algumas dados da Activity salva.
   */
  public ActivityResponse create(ActivityRequest activityRequest) {
    User user = getCurrentUser();
    Activity activitySaved = activityRepository.save(activityRequest.toActivity(user));

    return new ActivityResponse(activitySaved);
  }

  /**
   * Atualiza dados de uma Activity.
   *
   * @param id              da Activity a ser atualizada.
   * @param activityRequest novos dados da Activity a ser atualizada.
   */
  public void update(long id, ActivityRequest activityRequest) {
    Activity activityToBeUpdated = findByIdAndNotDeleted(id);
    activityToBeUpdated.update(activityRequest);
    activityRepository.save(activityToBeUpdated);
  }

  /**
   * Muda o {@code status} de uma Activity para DELETED
   *
   * @param id da Activity a ser deletada.
   */
  public void deleteActivityById(long id) {
    updateStatusById(id, Status.DELETED);
  }

  /**
   * Muda o {@code status} de uma Activity.
   *
   * @param id                  da Activity que terá status atualizado.
   * @param updateStatusRequest que contém o novo status da Activity.
   */
  public void updateStatusById(long id, UpdateStatusRequest updateStatusRequest) {
    updateStatusById(id, updateStatusRequest.toStatus());
  }

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

  private User getCurrentUser() {
    return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }

}
