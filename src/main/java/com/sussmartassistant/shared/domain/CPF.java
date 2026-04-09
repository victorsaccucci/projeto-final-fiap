package com.sussmartassistant.shared.domain;

/**
 * Value Object que representa um CPF válido.
 * Valida os dois dígitos verificadores usando o algoritmo padrão brasileiro.
 */
public record CPF(String valor) {

    public CPF {
        if (!isValid(valor)) {
            throw new DomainException("CPF inválido: " + valor);
        }
    }

    private static boolean isValid(String cpf) {
        if (cpf == null) return false;

        // Remove formatação (pontos e traço)
        String digits = cpf.replaceAll("[.\\-]", "");

        if (digits.length() != 11) return false;
        if (!digits.matches("\\d{11}")) return false;

        // Rejeita CPFs com todos os dígitos iguais (ex: 111.111.111-11)
        if (digits.chars().distinct().count() == 1) return false;

        // Validação do primeiro dígito verificador
        int soma = 0;
        for (int i = 0; i < 9; i++) {
            soma += Character.getNumericValue(digits.charAt(i)) * (10 - i);
        }
        int primeiroDigito = 11 - (soma % 11);
        if (primeiroDigito >= 10) primeiroDigito = 0;
        if (Character.getNumericValue(digits.charAt(9)) != primeiroDigito) return false;

        // Validação do segundo dígito verificador
        soma = 0;
        for (int i = 0; i < 10; i++) {
            soma += Character.getNumericValue(digits.charAt(i)) * (11 - i);
        }
        int segundoDigito = 11 - (soma % 11);
        if (segundoDigito >= 10) segundoDigito = 0;
        return Character.getNumericValue(digits.charAt(10)) == segundoDigito;
    }
}
