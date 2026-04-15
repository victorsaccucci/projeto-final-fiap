package com.sussmartassistant.assistente.infrastructure;

import com.sussmartassistant.assistente.application.ConsultarResultadoIAUseCase;
import com.sussmartassistant.assistente.application.SolicitarHipotesesDiagnosticasUseCase;
import com.sussmartassistant.assistente.domain.SolicitacaoIA;
import com.sussmartassistant.assistente.infrastructure.dto.SolicitacaoIAResponse;
import com.sussmartassistant.assistente.infrastructure.dto.SolicitarHipotesesRequest;
import com.sussmartassistant.seguranca.infrastructure.UsuarioAutenticado;
import com.sussmartassistant.shared.infrastructure.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/assistente/solicitacoes")
@Validated
@Tag(name = "Assistente Inteligente", description = "Solicitação e consulta de hipóteses diagnósticas via IA")
public class AssistenteController {

    private final SolicitarHipotesesDiagnosticasUseCase solicitarHipoteses;
    private final ConsultarResultadoIAUseCase consultarResultado;

    public AssistenteController(SolicitarHipotesesDiagnosticasUseCase solicitarHipoteses,
                                 ConsultarResultadoIAUseCase consultarResultado) {
        this.solicitarHipoteses = solicitarHipoteses;
        this.consultarResultado = consultarResultado;
    }

    @PostMapping
    @Operation(summary = "Solicitar hipóteses diagnósticas",
            description = "Envia solicitação assíncrona de hipóteses diagnósticas ao assistente IA")
    @ApiResponse(responseCode = "202", description = "Solicitação aceita para processamento")
    @ApiResponse(responseCode = "400", description = "Dados inválidos",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<Map<String, UUID>> solicitar(
            @Valid @RequestBody SolicitarHipotesesRequest request,
            @AuthenticationPrincipal UsuarioAutenticado usuario) {
        UUID profissionalId = request.profissionalId() != null ? request.profissionalId() : usuario.referenciaId();
        UUID solicitacaoId = solicitarHipoteses.executar(
                request.pacienteId(), request.sintomas(), profissionalId);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(Map.of("solicitacaoId", solicitacaoId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar resultado da solicitação",
            description = "Retorna o status e resultado de uma solicitação de hipóteses diagnósticas")
    @ApiResponse(responseCode = "200", description = "Resultado encontrado")
    @ApiResponse(responseCode = "404", description = "Solicitação não encontrada",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<SolicitacaoIAResponse> consultar(@PathVariable UUID id) {
        SolicitacaoIA solicitacao = consultarResultado.executar(id);
        return ResponseEntity.ok(SolicitacaoIAResponse.from(solicitacao));
    }
}
