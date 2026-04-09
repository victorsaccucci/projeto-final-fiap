package com.sussmartassistant.shared.domain;

/**
 * Value Object que representa um código CID (Classificação Internacional de Doenças).
 * Não pode ser vazio.
 */
public record CID(String codigo) {

    public CID {
        if (codigo == null || codigo.isBlank()) {
            throw new DomainException("Código CID inválido: não pode ser vazio");
        }
    }
}
