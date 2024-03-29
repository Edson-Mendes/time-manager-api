package br.com.emendes.timemanagerapi.controller;

import br.com.emendes.timemanagerapi.controller.openapi.ActivityControllerOpenApi;
import br.com.emendes.timemanagerapi.dto.request.ActivityRequest;
import br.com.emendes.timemanagerapi.dto.request.UpdateStatusRequest;
import br.com.emendes.timemanagerapi.dto.response.ActivityResponse;
import br.com.emendes.timemanagerapi.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("/activities")
public class ActivityController implements ActivityControllerOpenApi {

  private final ActivityService activityService;

  @Override
  @GetMapping
  public ResponseEntity<Page<ActivityResponse>> find(
      @SortDefault.SortDefaults({
          @SortDefault(sort = "status", direction = Sort.Direction.ASC),
          @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC)
      }) Pageable pageable) {
    return ResponseEntity.ok(activityService.find(pageable));
  }

  @Override
  @PostMapping
  public ResponseEntity<ActivityResponse> create(
      @RequestBody @Valid ActivityRequest activityRequest, UriComponentsBuilder uriBuilder) {
    ActivityResponse activityResponse = activityService.create(activityRequest);

    URI uri = uriBuilder.path("/activities/{id}").build(activityResponse.getId());
    return ResponseEntity.created(uri).body(activityResponse);
  }

  @Override
  @PutMapping("/{id}")
  public ResponseEntity<Void> update(
      @PathVariable long id, @RequestBody @Valid ActivityRequest activityRequest) {
    activityService.update(id, activityRequest);
    return ResponseEntity.noContent().build();
  }

  @Override
  @PatchMapping("/{id}")
  public ResponseEntity<Void> updateStatus(
      @PathVariable long id, @RequestBody @Valid UpdateStatusRequest updateStatusRequest) {
    activityService.updateStatusById(id, updateStatusRequest);
    return ResponseEntity.noContent().build();
  }

  @Override
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable long id) {
    activityService.deleteActivityById(id);
    return ResponseEntity.noContent().build();
  }

}
