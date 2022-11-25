package br.com.emendes.timemanagerapi.controller;

import br.com.emendes.timemanagerapi.controller.openapi.IntervalControllerOpenApi;
import br.com.emendes.timemanagerapi.dto.request.IntervalRequest;
import br.com.emendes.timemanagerapi.dto.response.IntervalResponse;
import br.com.emendes.timemanagerapi.service.IntervalService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

// TODO: Pesquisar se há um nome melhor para representar o intervalo que é trabalhado na atividade.
@RequiredArgsConstructor
@RestController
@RequestMapping("/activities/{activityId}/intervals")
public class IntervalController implements IntervalControllerOpenApi {

  private final IntervalService intervalService;

  @Override
  @GetMapping
  public ResponseEntity<Page<IntervalResponse>> find(
      @PathVariable long activityId,
      @PageableDefault(direction = Sort.Direction.DESC, sort = "startedAt") Pageable pageable) {
    return ResponseEntity.ok(intervalService.find(activityId, pageable));
  }

  @Override
  @PostMapping
  public ResponseEntity<IntervalResponse> create(
      @PathVariable long activityId, @RequestBody @Valid IntervalRequest requestBody, UriComponentsBuilder uriBuilder) {
    IntervalResponse intervalResponse = intervalService.create(activityId, requestBody);

    URI uri = uriBuilder.path("/activities/{activityId}/intervals/{id}").build(activityId, intervalResponse.getId());
    return ResponseEntity.created(uri).body(intervalResponse);
  }

  @Override
  @DeleteMapping("{id}")
  public ResponseEntity<Void> delete(@PathVariable long activityId, @PathVariable long id) {
    intervalService.delete(activityId, id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

}
