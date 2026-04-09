package com.sussmartassistant.shared.infrastructure;

import com.sussmartassistant.shared.application.CadastrarUnidadeSaudeUseCase;
import com.sussmartassistant.shared.application.ListarUnidadesSaudeUseCase;
import com.sussmartassistant.shared.domain.UnidadeSaude;
import com.sussmartassistant.shared.infrastructure.dto.CadastrarUnidadeSaudeRequest;
import com.sussmartassistant.shared.infrastructure.dto.UnidadeSaudeResponse;
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

import java.util.List;

@RestController
@RequestMapping("/api/v1/unidades-saude")
@Validated
@Tag(name = "Unidades de Saúde", description = "Cadastro e consulta de unidades de saúde")
public class UnidadeSaudeController {

    private final CadastrarUnidadeSaudeUseCase cadastrarUnidadeSaude;
    private final ListarUnidadesSaudeUseCase listarUnidadesSaude;

    public UnidadeSaudeController(CadastrarUnidadeSaudeUseCase cadastrarUnidadeSaude,
                                   ListarUnidadesSaudeUseCase listarUnidadesSaude) {
        this.cadastrarUnidadeSaude = cadastrarUnidadeSaude;
        this.listarUnidadesSaude = listarUnidadesSaude;
    }

    @PostMapping
    @Operation(summary = "Cadastrar unidade de saúde", description = "Cadastra uma nova unidade de saúde no sistema")
    @ApiResponse(responseCode = "201", description = "Unidade cadastrada com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos ou CNES duplicado",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<UnidadeSaudeResponse> cadastrar(@Valid @RequestBody CadastrarUnidadeSaudeRequest request) {
        UnidadeSaude unidade = cadastrarUnidadeSaude.executar(
                request.nome(), request.cnes(), request.endereco(),
                request.tipo(), request.especialidadesDisponiveis());
        return ResponseEntity.status(HttpStatus.CREATED).body(UnidadeSaudeResponse.from(unidade));
    }

    @GetMapping
    @Operation(summary = "Listar unidades de saúde", description = "Retorna todas as unidades de saúde cadastradas")
    @ApiResponse(responseCode = "200", description = "Lista de unidades de saúde")
    public ResponseEntity<List<UnidadeSaudeResponse>> listar() {
        List<UnidadeSaudeResponse> unidades = listarUnidadesSaude.executar().stream()
                .map(UnidadeSaudeResponse::from)
                .toList();
        return ResponseEntity.ok(unidades);
    }
}
