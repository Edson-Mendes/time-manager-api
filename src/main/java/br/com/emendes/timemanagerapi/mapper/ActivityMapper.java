package br.com.emendes.timemanagerapi.mapper;

import br.com.emendes.timemanagerapi.dto.request.ActivityRequest;
import br.com.emendes.timemanagerapi.dto.response.ActivityResponse;
import br.com.emendes.timemanagerapi.model.entity.Activity;

/**
 * Interface component que contém as abstrações de mapeamento de DTOs para a entidade Activity e vice-versa.
 */
public interface ActivityMapper {

  /**
   * Mapeia o DTO ActivityRequest para a entidade Activity.
   *
   * @param activityRequest que deve ser mapeado para Activity
   * @return Activity com dados vindo de activityRequest.
   */
  Activity toActivity(ActivityRequest activityRequest);

  /**
   * Mapeia a entidade Activity para o DTO ActivityResponse.
   *
   * @param activity que deve ser mapeado para ActivityResponse.
   * @return ActivityResponse com dados vindo de Activity.
   */
  ActivityResponse toActivityResponse(Activity activity);

  /**
   * Mescla as informações de ActivityRequest com as informações da Activity.
   * @param activityRequest com as novas informações da Activity.
   * @param activity Activity que receberá as novas informações.
   */
  void merge(ActivityRequest activityRequest, Activity activity);

}
