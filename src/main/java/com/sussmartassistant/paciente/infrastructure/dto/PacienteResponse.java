package com.sussmartassistant.paciente.infrastructure.dto;

import com.sussmartassistant.paciente.domain.Paciente;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.util.UUID;

@Schema(description = "Dados do paciente")
public record PacienteResponse(
        UUID id,
        String nome,
        String cpf,
        String cns,
        LocalDate dataNascimento,
        @Schema(description = "Idade calculada em anos") int idade,
        String sexo,
        String contato,
        Instant criadoEm,
        Instant atualizadoEm
) {
    public static PacienteResponse from(Paciente p) {
        int idade = p.getDataNascimento() != null
                ? Period.between(p.getDataNascimento(), LocalDate.now()).getYears()
                : 0;
        return new PacienteResponse(
                p.getId(), p.getNome(), p.getCpf().valor(),
                p.getCns() != null ? p.getCns().valor() : null,
                p.getDataNascimento(), idade, p.getSexo(), p.getContato(),
                p.getCriadoEm(), p.getAtualizadoEm()
        );
    }
}
