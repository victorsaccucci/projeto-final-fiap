package com.sussmartassistant.metricas.infrastructure.dto;

import com.sussmartassistant.metricas.domain.DiagnosticoFrequente;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Diagnóstico frequente com contagem de ocorrências")
public record DiagnosticoFrequenteResponse(
        @Schema(description = "Código CID do diagnóstico") String cid,
        @Schema(description = "Descrição do diagnóstico") String descricao,
        @Schema(description = "Quantidade de ocorrências") long count
) {
    public static DiagnosticoFrequenteResponse from(DiagnosticoFrequente d) {
        return new DiagnosticoFrequenteResponse(d.cid(), d.descricao(), d.count());
    }
}
