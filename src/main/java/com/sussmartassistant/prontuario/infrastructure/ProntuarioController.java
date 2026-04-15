package com.sussmartassistant.prontuario.infrastructure;

import com.sussmartassistant.prontuario.application.*;
import com.sussmartassistant.prontuario.domain.Exame;
import com.sussmartassistant.prontuario.domain.RegistroAtendimento;
import com.sussmartassistant.prontuario.infrastructure.dto.*;
import com.sussmartassistant.seguranca.infrastructure.UsuarioAutenticado;
import com.sussmartassistant.shared.domain.PageResult;
import com.sussmartassistant.shared.infrastructure.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/prontuarios")
@Validated
@Tag(name = "Prontuários", description = "Gestão de prontuários eletrônicos, atendimentos e exames")
public class ProntuarioController {

    private final ConsultarProntuarioCompletoUseCase consultarProntuarioCompleto;
    private final RegistrarAtendimentoUseCase registrarAtendimento;
    private final ListarAtendimentosUseCase listarAtendimentos;
    private final RegistrarExameUseCase registrarExame;
    private final ListarExamesUseCase listarExames;

    public ProntuarioController(ConsultarProntuarioCompletoUseCase consultarProntuarioCompleto,
                                 RegistrarAtendimentoUseCase registrarAtendimento,
                                 ListarAtendimentosUseCase listarAtendimentos,
                                 RegistrarExameUseCase registrarExame,
                                 ListarExamesUseCase listarExames) {
        this.consultarProntuarioCompleto = consultarProntuarioCompleto;
        this.registrarAtendimento = registrarAtendimento;
        this.listarAtendimentos = listarAtendimentos;
        this.registrarExame = registrarExame;
        this.listarExames = listarExames;
    }

    @GetMapping("/{pacienteId}")
    @Operation(summary = "Consultar prontuário completo", description = "Retorna prontuário com alergias, medicamentos, atendimentos e exames")
    @ApiResponse(responseCode = "200", description = "Prontuário encontrado")
    @ApiResponse(responseCode = "404", description = "Prontuário não encontrado",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<ProntuarioCompletoResponse> consultarProntuario(@PathVariable UUID pacienteId) {
        ProntuarioCompleto completo = consultarProntuarioCompleto.executar(pacienteId);
        return ResponseEntity.ok(ProntuarioCompletoResponse.from(completo));
    }

    @PostMapping("/{pacienteId}/atendimentos")
    @Operation(summary = "Registrar atendimento", description = "Registra um novo atendimento no prontuário do paciente")
    @ApiResponse(responseCode = "201", description = "Atendimento registrado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Prontuário não encontrado",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<AtendimentoResponse> registrarAtendimento(
            @PathVariable UUID pacienteId,
            @Valid @RequestBody RegistrarAtendimentoRequest request,
            @AuthenticationPrincipal UsuarioAutenticado usuario) {
        UUID profissionalId = request.profissionalId() != null ? request.profissionalId() : usuario.referenciaId();
        RegistroAtendimento atendimento = registrarAtendimento.executar(
                pacienteId, profissionalId, request.unidadeSaudeId(),
                request.data(), request.queixaPrincipal(), request.anamnese(),
                request.diagnosticoCid(), request.prescricoes(), request.observacoes());
        return ResponseEntity.status(HttpStatus.CREATED).body(AtendimentoResponse.from(atendimento));
    }

    @GetMapping("/{pacienteId}/atendimentos")
    @Operation(summary = "Listar atendimentos", description = "Lista atendimentos paginados com filtros opcionais")
    @ApiResponse(responseCode = "200", description = "Lista de atendimentos")
    @ApiResponse(responseCode = "404", description = "Prontuário não encontrado",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<PageResult<AtendimentoResponse>> listarAtendimentos(
            @PathVariable UUID pacienteId,
            @Parameter(description = "Data início do período") @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @Parameter(description = "Data fim do período") @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim,
            @Parameter(description = "Código CID para filtro") @RequestParam(required = false) String cid,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageResult<RegistroAtendimento> result = listarAtendimentos.executar(pacienteId, inicio, fim, cid, page, size);
        PageResult<AtendimentoResponse> response = PageResult.of(
                result.content().stream().map(AtendimentoResponse::from).toList(),
                result.page(), result.size(), result.totalElements());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{pacienteId}/exames")
    @Operation(summary = "Registrar exame", description = "Registra um novo exame no prontuário do paciente")
    @ApiResponse(responseCode = "201", description = "Exame registrado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Prontuário não encontrado",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<ExameResponse> registrarExame(
            @PathVariable UUID pacienteId,
            @Valid @RequestBody RegistrarExameRequest request,
            @AuthenticationPrincipal UsuarioAutenticado usuario) {
        UUID registradoPor = request.registradoPorId() != null ? request.registradoPorId() : usuario.referenciaId();
        Exame exame = registrarExame.executar(
                pacienteId, request.tipo(), request.dataRealizacao(), request.resultado(),
                request.unidadeSaudeOrigemId(), registradoPor);
        return ResponseEntity.status(HttpStatus.CREATED).body(ExameResponse.from(exame));
    }

    @GetMapping("/{pacienteId}/exames")
    @Operation(summary = "Listar exames", description = "Lista exames paginados com filtros opcionais")
    @ApiResponse(responseCode = "200", description = "Lista de exames")
    @ApiResponse(responseCode = "404", description = "Prontuário não encontrado",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<PageResult<ExameResponse>> listarExames(
            @PathVariable UUID pacienteId,
            @Parameter(description = "Tipo de exame para filtro") @RequestParam(required = false) String tipo,
            @Parameter(description = "Data início do período") @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @Parameter(description = "Data fim do período") @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageResult<Exame> result = listarExames.executar(pacienteId, tipo, inicio, fim, page, size);
        PageResult<ExameResponse> response = PageResult.of(
                result.content().stream().map(ExameResponse::from).toList(),
                result.page(), result.size(), result.totalElements());
        return ResponseEntity.ok(response);
    }
}
