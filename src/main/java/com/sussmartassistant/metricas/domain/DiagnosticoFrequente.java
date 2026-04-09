package com.sussmartassistant.metricas.domain;

/**
 * Representa um diagnóstico (CID) com sua frequência de ocorrência.
 */
public record DiagnosticoFrequente(
        String cid,
        String descricao,
        long count
) {}
