package com.sussmartassistant.paciente.infrastructure.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Schema(description = "Dados para cadastro de novo paciente")
public record CadastrarPacienteRequest(
        @NotBlank(message = "Nome é obrigatório")
        @Schema(description = "Nome completo do paciente", example = "Maria da Silva")
        String nome,

        @NotBlank(message = "CPF é obrigatório")
        @Schema(description = "CPF do paciente (11 dígitos)", example = "52998224725")
        String cpf,

        @Schema(description = "Cartão Nacional de Saúde (15 dígitos)", example = "123456789012345")
        String cns,

        @NotNull(message = "Data de nascimento é obrigatória")
        @Schema(description = "Data de nascimento", example = "1985-03-15")
        LocalDate dataNascimento,

        @NotBlank(message = "Sexo é obrigatório")
        @Schema(description = "Sexo do paciente", example = "F")
        String sexo,

        @Schema(description = "Contato do paciente (telefone ou email)", example = "(11) 99999-0000")
        String contato
) {}
