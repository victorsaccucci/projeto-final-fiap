package com.sussmartassistant.assistente.infrastructure.dto;

import com.sussmartassistant.assistente.domain.HipoteseDiagnostica;
import com.sussmartassistant.shared.domain.NivelConfianca;

import java.util.UUID;

public record HipoteseDiagnosticaResponse(
        UUID id,
        String codigoCid,
        String justificativa,
        NivelConfianca confianca,
        boolean reincidencia,
        String alertaContraindicacao,
        int ordem
) {
    public static HipoteseDiagnosticaResponse from(HipoteseDiagnostica h) {
        return new HipoteseDiagnosticaResponse(
                h.getId(), h.getCodigoCid(), h.getJustificativa(),
                h.getConfianca(), h.isReincidencia(),
                h.getAlertaContraindicacao(), h.getOrdem()
        );
    }
}
