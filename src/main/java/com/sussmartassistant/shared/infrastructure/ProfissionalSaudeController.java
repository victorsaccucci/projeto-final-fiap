package com.sussmartassistant.shared.infrastructure;

import com.sussmartassistant.shared.application.CadastrarProfissionalUseCase;
import com.sussmartassistant.shared.domain.ProfissionalSaude;
import com.sussmartassistant.shared.infrastructure.dto.CadastrarProfissionalRequest;
import com.sussmartassistant.shared.infrastructure.dto.ProfissionalSaudeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profissionais")
@Validated
@Tag(name = "Profissionais de Saúde", description = "Cadastro de profissionais de saúde")
public class ProfissionalSaudeController {

    private final CadastrarProfissionalUseCase cadastrarProfissional;

    public ProfissionalSaudeController(CadastrarProfissionalUseCase cadastrarProfissional) {
        this.cadastrarProfissional = cadastrarProfissional;
    }

    @PostMapping
    @Operation(summary = "Cadastrar profissional de saúde", description = "Cadastra um novo profissional de saúde no sistema")
    @ApiResponse(responseCode = "201", description = "Profissional cadastrado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos ou CRM duplicado",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<ProfissionalSaudeResponse> cadastrar(@Valid @RequestBody CadastrarProfissionalRequest request) {
        ProfissionalSaude profissional = cadastrarProfissional.executar(
                request.nome(), request.registroProfissional(),
                request.especialidade(), request.unidadeSaudeId());
        return ResponseEntity.status(HttpStatus.CREATED).body(ProfissionalSaudeResponse.from(profissional));
    }
}
