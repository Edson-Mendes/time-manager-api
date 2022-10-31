package br.com.emendes.timemanagerapi.controller.openapi;

import br.com.emendes.timemanagerapi.dto.request.LoginRequest;
import br.com.emendes.timemanagerapi.dto.response.TokenResponse;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@OpenAPIDefinition(tags = @Tag(name = "Signin"))
public interface SigninControllerOpenApi {

  @Operation(summary = "Signin", tags = {"Signin"})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Signin com sucesso"),
      @ApiResponse(responseCode = "400", description = "Credenciais inválidas", content = @Content),
  })
  ResponseEntity<TokenResponse> signin(LoginRequest loginRequest);

}
