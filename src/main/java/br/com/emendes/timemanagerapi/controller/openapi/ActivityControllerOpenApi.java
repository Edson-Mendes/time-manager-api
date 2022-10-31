package br.com.emendes.timemanagerapi.controller.openapi;

import br.com.emendes.timemanagerapi.dto.request.ActivityRequest;
import br.com.emendes.timemanagerapi.dto.request.UpdateStatusRequest;
import br.com.emendes.timemanagerapi.dto.response.ActivityResponse;
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

@OpenAPIDefinition(tags = @Tag(name = "Activity"), security = {@SecurityRequirement(name = "bearer-key")})
public interface ActivityControllerOpenApi {

  @Operation(summary = "Buscar todas as Activities do usuário", tags = {"Activity"})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Encontrou activities com sucesso"),
      @ApiResponse(responseCode = "400", description = "Algo de errado com a requisição",
          content = @Content),
      @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
  })
  ResponseEntity<Page<ActivityResponse>> find(@ParameterObject Pageable pageable);

  @Operation(summary = "Salvar uma Activity", tags = {"Activity"})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Activity salva"),
      @ApiResponse(responseCode = "400", description = "Algo de errado com a requisição",
          content = @Content),
      @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
  })
  ResponseEntity<ActivityResponse> create(ActivityRequest activityRequest, UriComponentsBuilder uriBuilder);

  @Operation(summary = "Atualizar Activity por id", tags = {"Activity"})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Activity atualizado com sucesso"),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "400", description = "Algo de errado com a requisição"),
      @ApiResponse(responseCode = "404", description = "Activity não encontrada"),
  })
  ResponseEntity<Void> update(long id, ActivityRequest activityRequest);

  @Operation(summary = "Atualizar status da Activity", tags = {"Activity"})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Status atualizado com sucesso"),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "400", description = "Algo de errado com a requisição"),
      @ApiResponse(responseCode = "404", description = "Activity não encontrada"),
  })
  ResponseEntity<Void> updateStatus(long id, UpdateStatusRequest updateStatusRequest);

  @Operation(summary = "Deletar Activity por id", tags = {"Activity"})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Deletado com sucesso"),
      @ApiResponse(responseCode = "400", description = "Algo de errado com a requisição"),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "404", description = "Activity não encontrada"),
  })
  ResponseEntity<Void> delete(long id);
}
