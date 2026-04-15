package com.sussmartassistant.paciente.infrastructure.dto;

import com.sussmartassistant.shared.domain.GravidadeAlergia;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Dados para registro de alergia")
public record RegistrarAlergiaRequest(
        @NotBlank(message = "Substância é obrigatória")
        @Schema(description = "Substância causadora da alergia", example = "Penicilina")
        String substancia,

        @NotNull(message = "Gravidade é obrigatória")
        @Schema(description = "Gravidade da alergia", example = "CRITICA")
        GravidadeAlergia gravidade,

        @Schema(description = "Reação observada", example = "Edema de glote")
        String reacaoObservada,

        @Schema(description = "ID do profissional que registrou (opcional, usa o do token JWT)")
        java.util.UUID registradoPorId
) {}
