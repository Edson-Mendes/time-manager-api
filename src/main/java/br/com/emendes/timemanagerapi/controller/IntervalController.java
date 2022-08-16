package br.com.emendes.timemanagerapi.controller;

import br.com.emendes.timemanagerapi.dto.request.IntervalRequestBody;
import br.com.emendes.timemanagerapi.dto.response.IntervalResponseBody;
import br.com.emendes.timemanagerapi.service.IntervalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

// TODO: Pesquisar se há um nome melhor para representar o intervalo onde é trabalho na atividade.
@RequiredArgsConstructor
@RestController
@RequestMapping("/activities/{activityId}/intervals")
public class IntervalController {

  private final IntervalService intervalService;

  //  TODO: Fazer um handler para lidar com tentativa de converter algo que não seja long para long.
  @PostMapping
  public ResponseEntity<IntervalResponseBody> create(
      @PathVariable long activityId, @RequestBody IntervalRequestBody requestBody, UriComponentsBuilder uriBuilder) {
    IntervalResponseBody intervalResponseBody = intervalService.create(activityId, requestBody);

    URI uri = uriBuilder.path("/activities/{activityId}/intervals/{id}").build(activityId, intervalResponseBody.getId());
    return ResponseEntity.created(uri).body(intervalResponseBody);
  }

}
