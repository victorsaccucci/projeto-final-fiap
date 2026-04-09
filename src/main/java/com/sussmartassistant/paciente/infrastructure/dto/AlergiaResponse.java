package com.sussmartassistant.paciente.infrastructure.dto;

import com.sussmartassistant.paciente.domain.Alergia;
import com.sussmartassistant.shared.domain.GravidadeAlergia;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.UUID;

@Schema(description = "Dados de uma alergia registrada")
public record AlergiaResponse(
        UUID id,
        UUID pacienteId,
        String substancia,
        GravidadeAlergia gravidade,
        String reacaoObservada,
        Instant registradoEm,
        UUID registradoPorId
) {
    public static AlergiaResponse from(Alergia a) {
        return new AlergiaResponse(a.getId(), a.getPacienteId(), a.getSubstancia(),
                a.getGravidade(), a.getReacaoObservada(), a.getRegistradoEm(), a.getRegistradoPorId());
    }
}
