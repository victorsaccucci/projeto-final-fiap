package com.sussmartassistant.shared.domain;

import java.util.List;
import java.util.UUID;

/**
 * Entidade de domínio UnidadeSaude — POJO puro, sem dependências de framework.
 */
public class UnidadeSaude {

    private UUID id;
    private String nome;
    private String cnes;
    private String endereco;
    private TipoUnidadeSaude tipo;
    private List<String> especialidadesDisponiveis;

    public UnidadeSaude(UUID id, String nome, String cnes, String endereco,
                        TipoUnidadeSaude tipo, List<String> especialidadesDisponiveis) {
        this.id = id;
        this.nome = nome;
        this.cnes = cnes;
        this.endereco = endereco;
        this.tipo = tipo;
        this.especialidadesDisponiveis = especialidadesDisponiveis;
    }

    public static UnidadeSaude criar(String nome, String cnes, String endereco,
                                      TipoUnidadeSaude tipo, List<String> especialidadesDisponiveis) {
        return new UnidadeSaude(UUID.randomUUID(), nome, cnes, endereco, tipo, especialidadesDisponiveis);
    }

    public UUID getId() { return id; }
    public String getNome() { return nome; }
    public String getCnes() { return cnes; }
    public String getEndereco() { return endereco; }
    public TipoUnidadeSaude getTipo() { return tipo; }
    public List<String> getEspecialidadesDisponiveis() { return especialidadesDisponiveis; }
}
