package br.com.emendes.timemanagerapi.controller.openapi;

import br.com.emendes.timemanagerapi.dto.request.SignupRequest;
import br.com.emendes.timemanagerapi.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

@OpenAPIDefinition(tags = @Tag(name = "Signup"))
public interface SignupControllerOpenApi {

  @Operation(summary = "Signup", tags = {"Signup"})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Signup com sucesso"),
      @ApiResponse(responseCode = "400", description = "Algo de errado com a requisição", content = @Content),
  })
  ResponseEntity<UserResponse> signup(SignupRequest signupRequest, UriComponentsBuilder uriBuilder);

}
