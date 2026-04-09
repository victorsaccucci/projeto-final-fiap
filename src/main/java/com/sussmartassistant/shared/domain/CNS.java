package com.sussmartassistant.shared.domain;

/**
 * Value Object que representa um Cartão Nacional de Saúde (CNS).
 * Deve conter exatamente 15 caracteres numéricos.
 */
public record CNS(String valor) {

    public CNS {
        if (valor == null || valor.length() != 15 || !valor.matches("\\d{15}")) {
            throw new DomainException("CNS inválido: deve conter exatamente 15 dígitos numéricos");
        }
    }
}
