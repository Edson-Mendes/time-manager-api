package br.com.emendes.timemanagerapi.controller;

import br.com.emendes.timemanagerapi.dto.request.ActivityRequestBody;
import br.com.emendes.timemanagerapi.dto.response.ActivityResponseBody;
import br.com.emendes.timemanagerapi.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
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

  @PostMapping
  public ResponseEntity<ActivityResponseBody> create(
      @RequestBody @Valid ActivityRequestBody activityRequestBody, UriComponentsBuilder uriBuilder) {
    ActivityResponseBody activityResponseBody = activityService.create(activityRequestBody);

    URI uri = uriBuilder.path("/activities/{id}").build(activityResponseBody.getId());
    return ResponseEntity.created(uri).body(activityResponseBody);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Void> update(
      @PathVariable long id, @RequestBody @Valid ActivityRequestBody activityRequestBody) {
    activityService.update(id, activityRequestBody);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable long id) {
    activityService.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
