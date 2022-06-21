package br.com.emendes.timemanagerapi.controller;

import br.com.emendes.timemanagerapi.dto.response.ActivityResponseBody;
import br.com.emendes.timemanagerapi.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/activities")
public class ActivityController {

  private final ActivityService activityService;

  @GetMapping
  public ResponseEntity<List<ActivityResponseBody>> findAll(){
    List<ActivityResponseBody> activities = activityService.findAll();

    return ResponseEntity.ok(activities);
  }

}
