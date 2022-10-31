package br.com.emendes.timemanagerapi.controller.openapi;

import br.com.emendes.timemanagerapi.dto.request.IntervalRequest;
import br.com.emendes.timemanagerapi.dto.response.IntervalResponseBody;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

@OpenAPIDefinition(tags = @Tag(name = "Interval"), security = {@SecurityRequirement(name = "bearer-key")})
public interface IntervalControllerOpenApi {

  @Operation(summary = "Buscar todos os intervals de uma dada Activity do usuário", tags = {"Interval"})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Encontrou Intervals com sucesso"),
      @ApiResponse(responseCode = "400", description = "Algo de errado com a requisição", content = @Content),
      @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
      @ApiResponse(responseCode = "404", description = "Activity não encontrada", content = @Content),
  })
  ResponseEntity<Page<IntervalResponseBody>> find(long activityId, @ParameterObject Pageable pageable);

  @Operation(summary = "Salvar um Interval", tags = {"Interval"})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Interval salvo com sucesso"),
      @ApiResponse(responseCode = "400", description = "Algo de errado com a requisição", content = @Content),
      @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
      @ApiResponse(responseCode = "404", description = "Activity não encontrada", content = @Content),
  })
  ResponseEntity<IntervalResponseBody> create(
      long activityId, IntervalRequest requestBody, UriComponentsBuilder uriBuilder);

  @Operation(summary = "Deletar Interval por id", tags = {"Interval"})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Deletado com sucesso"),
      @ApiResponse(responseCode = "400", description = "Algo de errado com a requisição"),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "404", description = "Activity ou Interval não encontrado"),
  })
  ResponseEntity<Void> delete(long activityId, long id);

}
