package br.com.emendes.timemanagerapi.service;

import br.com.emendes.timemanagerapi.dto.request.ActivityRequest;
import br.com.emendes.timemanagerapi.dto.request.UpdateStatusRequest;
import br.com.emendes.timemanagerapi.dto.response.ActivityResponse;
import br.com.emendes.timemanagerapi.model.entity.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ActivityService {

  /**
   * Busca paginada de Activity do usuário logado.
   *
   * @param pageable informações da paginação
   * @return {@code Page<ActivityResponse>} contendo as Activities do usuário.
   */
  Page<ActivityResponse> find(Pageable pageable);

  /**
   * Busca Activity por id do usuário logado.
   *
   * @param id da Activity desejada.
   * @return A Activity buscada.
   */
  Activity findById(long id);

  /**
   * Cria uma Activity relacionada ao usuário logado.
   *
   * @param activityRequest dados da Activity a ser salva.
   * @return ActivityResponse - algumas dados da Activity salva.
   */
  ActivityResponse create(ActivityRequest activityRequest);

  /**
   * Atualiza dados de uma Activity.
   *
   * @param id              da Activity a ser atualizada.
   * @param activityRequest novos dados da Activity a ser atualizada.
   */
  void update(long id, ActivityRequest activityRequest);

  /**
   * Muda o {@code status} de uma Activity para DELETED
   *
   * @param id da Activity a ser deletada.
   */
  void deleteActivityById(long id);

  /**
   * Atualizar o {@code status} de uma Activity.
   *
   * @param id                  da Activity que terá status atualizado.
   * @param updateStatusRequest que contém o novo status da Activity.
   */
  void updateStatusById(long id, UpdateStatusRequest updateStatusRequest);

}
