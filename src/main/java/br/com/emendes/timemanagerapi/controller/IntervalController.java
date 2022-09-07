package br.com.emendes.timemanagerapi.controller;

import br.com.emendes.timemanagerapi.dto.request.IntervalRequestBody;
import br.com.emendes.timemanagerapi.dto.response.IntervalResponseBody;
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

import java.net.URI;

// TODO: Pesquisar se há um nome melhor para representar o intervalo que é trabalhado na atividade.
@RequiredArgsConstructor
@RestController
@RequestMapping("/activities/{activityId}/intervals")
public class IntervalController {

  private final IntervalService intervalService;

  @GetMapping
  public ResponseEntity<Page<IntervalResponseBody>> find(
      @PathVariable long activityId,
      @PageableDefault(direction = Sort.Direction.DESC, sort = "startedAt") Pageable pageable){
    return ResponseEntity.ok(intervalService.find(activityId, pageable));
  }

  //  TODO: Fazer um handler para lidar com tentativa de converter algo que não seja long para long.
  @PostMapping
  public ResponseEntity<IntervalResponseBody> create(
      @PathVariable long activityId, @RequestBody IntervalRequestBody requestBody, UriComponentsBuilder uriBuilder) {
    IntervalResponseBody intervalResponseBody = intervalService.create(activityId, requestBody);

    URI uri = uriBuilder.path("/activities/{activityId}/intervals/{id}").build(activityId, intervalResponseBody.getId());
    return ResponseEntity.created(uri).body(intervalResponseBody);
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> delete(@PathVariable long activityId, @PathVariable long id){
    intervalService.delete(activityId, id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

}
