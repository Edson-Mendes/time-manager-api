package br.com.emendes.timemanagerapi.service;

import br.com.emendes.timemanagerapi.dto.request.ActivityRequest;
import br.com.emendes.timemanagerapi.dto.request.UpdateStatusRequest;
import br.com.emendes.timemanagerapi.dto.response.ActivityResponse;
import br.com.emendes.timemanagerapi.model.entity.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ActivityService {

  Page<ActivityResponse> find(Pageable pageable);

  Activity findById(long id);

  ActivityResponse create(ActivityRequest activityRequest);

  void update(long id, ActivityRequest activityRequest);

  void deleteActivityById(long id);

  void updateStatusById(long id, UpdateStatusRequest updateStatusRequest);

}
