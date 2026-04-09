package com.sussmartassistant.paciente.infrastructure;

import com.sussmartassistant.paciente.domain.Alergia;
import com.sussmartassistant.shared.domain.GravidadeAlergia;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "alergias", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"paciente_id", "substancia"})
})
public class AlergiaEntity {

    @Id
    private UUID id;

    @Column(name = "paciente_id", nullable = false)
    private UUID pacienteId;

    @Column(nullable = false)
    private String substancia;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GravidadeAlergia gravidade;

    private String reacaoObservada;

    @Column(nullable = false, updatable = false)
    private Instant registradoEm;

    private UUID registradoPorId;

    protected AlergiaEntity() {}

    public static AlergiaEntity fromDomain(Alergia a) {
        AlergiaEntity e = new AlergiaEntity();
        e.id = a.getId();
        e.pacienteId = a.getPacienteId();
        e.substancia = a.getSubstancia();
        e.gravidade = a.getGravidade();
        e.reacaoObservada = a.getReacaoObservada();
        e.registradoEm = a.getRegistradoEm();
        e.registradoPorId = a.getRegistradoPorId();
        return e;
    }

    public Alergia toDomain() {
        return new Alergia(id, pacienteId, substancia, gravidade, reacaoObservada, registradoEm, registradoPorId);
    }
}
