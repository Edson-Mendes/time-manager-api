package br.com.emendes.timemanagerapi.service;

import br.com.emendes.timemanagerapi.dto.request.IntervalRequest;
import br.com.emendes.timemanagerapi.dto.response.IntervalResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IntervalService {

  IntervalResponse create(long activityId, IntervalRequest requestBody);

  Page<IntervalResponse> find(long activityId, Pageable pageable);

  void delete(long activityId, long intervalId);

}
