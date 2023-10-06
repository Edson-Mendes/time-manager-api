package br.com.emendes.timemanagerapi.mapper.impl;

import br.com.emendes.timemanagerapi.dto.request.ActivityRequest;
import br.com.emendes.timemanagerapi.dto.response.ActivityResponse;
import br.com.emendes.timemanagerapi.mapper.ActivityMapper;
import br.com.emendes.timemanagerapi.model.entity.Activity;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Implementação de {@link ActivityMapper}.
 */
@Component
public class ActivityMapperImpl implements ActivityMapper {

  /**
   * @throws IllegalArgumentException se activityRequest for null.
   */
  @Override
  public Activity toActivity(ActivityRequest activityRequest) {
    Assert.notNull(activityRequest, "activityRequest must not be null");

    return Activity.builder()
        .name(activityRequest.getName())
        .description(activityRequest.getDescription())
        .build();
  }

  /**
   * @throws IllegalArgumentException se activity for null.
   */
  @Override
  public ActivityResponse toActivityResponse(Activity activity) {
    Assert.notNull(activity, "activity must not be null");

    return ActivityResponse.builder()
        .id(activity.getId())
        .name(activity.getName())
        .description(activity.getDescription())
        .status(activity.getStatus())
        .createdAt(activity.getCreatedAt())
        .build();
  }

  /**
   * @throws IllegalArgumentException se activityRequest ou activity for null.
   */
  @Override
  public void merge(ActivityRequest activityRequest, Activity activity) {
    Assert.notNull(activityRequest, "activityRequest must not be null");
    Assert.notNull(activity, "activity must not be null");

    activity.setName(activityRequest.getName());
    activity.setDescription(activityRequest.getDescription());
  }

}
