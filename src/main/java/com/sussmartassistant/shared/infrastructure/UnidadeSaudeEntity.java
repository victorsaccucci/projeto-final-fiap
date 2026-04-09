package com.sussmartassistant.shared.infrastructure;

import com.sussmartassistant.shared.domain.TipoUnidadeSaude;
import com.sussmartassistant.shared.domain.UnidadeSaude;
import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "unidades_saude")
public class UnidadeSaudeEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String cnes;

    private String endereco;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoUnidadeSaude tipo;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "unidade_especialidades", joinColumns = @JoinColumn(name = "unidade_saude_id"))
    @Column(name = "especialidade")
    private List<String> especialidadesDisponiveis;

    protected UnidadeSaudeEntity() {}

    public static UnidadeSaudeEntity fromDomain(UnidadeSaude u) {
        UnidadeSaudeEntity e = new UnidadeSaudeEntity();
        e.id = u.getId();
        e.nome = u.getNome();
        e.cnes = u.getCnes();
        e.endereco = u.getEndereco();
        e.tipo = u.getTipo();
        e.especialidadesDisponiveis = u.getEspecialidadesDisponiveis();
        return e;
    }

    public UnidadeSaude toDomain() {
        return new UnidadeSaude(id, nome, cnes, endereco, tipo, especialidadesDisponiveis);
    }

    public UUID getId() { return id; }
    public String getCnes() { return cnes; }
}
