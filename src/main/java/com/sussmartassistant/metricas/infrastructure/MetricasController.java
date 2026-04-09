package com.sussmartassistant.metricas.infrastructure;

import com.sussmartassistant.metricas.application.ConsultarDiagnosticosFrequentesUseCase;
import com.sussmartassistant.metricas.application.ConsultarMetricasAssistenteUseCase;
import com.sussmartassistant.metricas.application.ConsultarMetricasAtendimentoUseCase;
import com.sussmartassistant.metricas.domain.MetricasAssistente;
import com.sussmartassistant.metricas.infrastructure.dto.DiagnosticoFrequenteResponse;
import com.sussmartassistant.metricas.infrastructure.dto.MetricasAssistenteResponse;
import com.sussmartassistant.metricas.infrastructure.dto.MetricasAtendimentoResponse;
import com.sussmartassistant.shared.infrastructure.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/metricas")
@Tag(name = "Métricas", description = "Métricas de atendimentos, diagnósticos e uso do assistente IA")
public class MetricasController {

    private final ConsultarMetricasAtendimentoUseCase consultarMetricasAtendimento;
    private final ConsultarDiagnosticosFrequentesUseCase consultarDiagnosticosFrequentes;
    private final ConsultarMetricasAssistenteUseCase consultarMetricasAssistente;

    public MetricasController(ConsultarMetricasAtendimentoUseCase consultarMetricasAtendimento,
                               ConsultarDiagnosticosFrequentesUseCase consultarDiagnosticosFrequentes,
                               ConsultarMetricasAssistenteUseCase consultarMetricasAssistente) {
        this.consultarMetricasAtendimento = consultarMetricasAtendimento;
        this.consultarDiagnosticosFrequentes = consultarDiagnosticosFrequentes;
        this.consultarMetricasAssistente = consultarMetricasAssistente;
    }

    @GetMapping("/atendimentos")
    @Operation(summary = "Métricas de atendimentos",
               description = "Retorna total de atendimentos por período e unidade de saúde")
    @ApiResponse(responseCode = "200", description = "Métricas retornadas com sucesso")
    @ApiResponse(responseCode = "401", description = "Não autenticado",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<MetricasAtendimentoResponse> metricasAtendimentos(
            @Parameter(description = "Data início do período")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @Parameter(description = "Data fim do período")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim,
            @Parameter(description = "ID da unidade de saúde (opcional)")
            @RequestParam(required = false) UUID unidadeId) {

        long total = consultarMetricasAtendimento.executar(inicio, fim, unidadeId);
        return ResponseEntity.ok(new MetricasAtendimentoResponse(total, inicio, fim, unidadeId));
    }

    @GetMapping("/diagnosticos")
    @Operation(summary = "Diagnósticos mais frequentes",
               description = "Retorna os CIDs mais frequentes por período e unidade de saúde")
    @ApiResponse(responseCode = "200", description = "Diagnósticos retornados com sucesso")
    @ApiResponse(responseCode = "401", description = "Não autenticado",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<List<DiagnosticoFrequenteResponse>> diagnosticosFrequentes(
            @Parameter(description = "Data início do período")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @Parameter(description = "Data fim do período")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim,
            @Parameter(description = "ID da unidade de saúde (opcional)")
            @RequestParam(required = false) UUID unidadeId) {

        List<DiagnosticoFrequenteResponse> diagnosticos = consultarDiagnosticosFrequentes
                .executar(inicio, fim, unidadeId).stream()
                .map(DiagnosticoFrequenteResponse::from)
                .toList();
        return ResponseEntity.ok(diagnosticos);
    }

    @GetMapping("/assistente")
    @Operation(summary = "Métricas do assistente IA",
               description = "Retorna métricas de uso do assistente inteligente")
    @ApiResponse(responseCode = "200", description = "Métricas retornadas com sucesso")
    @ApiResponse(responseCode = "401", description = "Não autenticado",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<MetricasAssistenteResponse> metricasAssistente(
            @Parameter(description = "Data início do período")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @Parameter(description = "Data fim do período")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {

        MetricasAssistente metricas = consultarMetricasAssistente.executar(inicio, fim);
        return ResponseEntity.ok(MetricasAssistenteResponse.from(metricas, inicio, fim));
    }
}
