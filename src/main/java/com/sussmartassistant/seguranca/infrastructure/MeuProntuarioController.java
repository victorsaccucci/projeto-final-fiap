package com.sussmartassistant.seguranca.infrastructure;

import com.sussmartassistant.prontuario.application.ConsultarProntuarioCompletoUseCase;
import com.sussmartassistant.prontuario.application.ProntuarioCompleto;
import com.sussmartassistant.prontuario.infrastructure.dto.ProntuarioCompletoResponse;
import com.sussmartassistant.seguranca.application.AuditoriaEventPublisher;
import com.sussmartassistant.shared.domain.TipoOperacaoAuditoria;
import com.sussmartassistant.shared.infrastructure.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * Endpoint para o paciente visualizar seu próprio prontuário.
 * Restrito a usuários com role PACIENTE — o referenciaId no JWT aponta para o Paciente.
 */
@RestController
@RequestMapping("/api/v1/meu-prontuario")
@Tag(name = "Meu Prontuário", description = "Visualização do prontuário pelo próprio paciente")
public class MeuProntuarioController {

    private final ConsultarProntuarioCompletoUseCase consultarProntuario;
    private final AuditoriaEventPublisher auditoriaPublisher;

    public MeuProntuarioController(ConsultarProntuarioCompletoUseCase consultarProntuario,
                                    AuditoriaEventPublisher auditoriaPublisher) {
        this.consultarProntuario = consultarProntuario;
        this.auditoriaPublisher = auditoriaPublisher;
    }

    @GetMapping
    @Operation(summary = "Visualizar meu prontuário",
               description = "Retorna o prontuário completo do paciente autenticado (somente leitura)")
    @ApiResponse(responseCode = "200", description = "Prontuário encontrado")
    @ApiResponse(responseCode = "403", description = "Acesso negado",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Prontuário não encontrado",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<ProntuarioCompletoResponse> meuProntuario(
            @AuthenticationPrincipal UsuarioAutenticado usuario) {

        UUID pacienteId = usuario.referenciaId();
        if (pacienteId == null) {
            throw new com.sussmartassistant.shared.domain.AccessDeniedException(
                    "Usuário não possui referência a um paciente");
        }

        ProntuarioCompleto completo = consultarProntuario.executar(pacienteId);

        auditoriaPublisher.publicar(
                usuario.userId(),
                TipoOperacaoAuditoria.LEITURA,
                completo.prontuario().getId(),
                "Paciente visualizou próprio prontuário");

        return ResponseEntity.ok(ProntuarioCompletoResponse.from(completo));
    }
}
