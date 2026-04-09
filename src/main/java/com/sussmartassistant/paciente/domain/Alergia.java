package com.sussmartassistant.paciente.domain;

import com.sussmartassistant.shared.domain.GravidadeAlergia;

import java.time.Instant;
import java.util.UUID;

/**
 * Entidade de domínio Alergia — POJO puro, sem dependências de framework.
 */
public class Alergia {

    private UUID id;
    private UUID pacienteId;
    private String substancia;
    private GravidadeAlergia gravidade;
    private String reacaoObservada;
    private Instant registradoEm;
    private UUID registradoPorId;

    public Alergia(UUID id, UUID pacienteId, String substancia, GravidadeAlergia gravidade,
                   String reacaoObservada, Instant registradoEm, UUID registradoPorId) {
        this.id = id;
        this.pacienteId = pacienteId;
        this.substancia = substancia;
        this.gravidade = gravidade;
        this.reacaoObservada = reacaoObservada;
        this.registradoEm = registradoEm;
        this.registradoPorId = registradoPorId;
    }

    public static Alergia criar(UUID pacienteId, String substancia, GravidadeAlergia gravidade,
                                 String reacaoObservada, UUID registradoPorId) {
        return new Alergia(UUID.randomUUID(), pacienteId, substancia, gravidade,
                reacaoObservada, Instant.now(), registradoPorId);
    }

    public UUID getId() { return id; }
    public UUID getPacienteId() { return pacienteId; }
    public String getSubstancia() { return substancia; }
    public GravidadeAlergia getGravidade() { return gravidade; }
    public String getReacaoObservada() { return reacaoObservada; }
    public Instant getRegistradoEm() { return registradoEm; }
    public UUID getRegistradoPorId() { return registradoPorId; }
}
