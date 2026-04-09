package com.sussmartassistant.prontuario.application;

import com.sussmartassistant.paciente.domain.Alergia;
import com.sussmartassistant.paciente.domain.MedicamentoEmUso;
import com.sussmartassistant.prontuario.domain.Exame;
import com.sussmartassistant.prontuario.domain.Prontuario;
import com.sussmartassistant.prontuario.domain.RegistroAtendimento;

import java.util.List;

/**
 * DTO de aplicação que agrega dados do prontuário completo de um paciente.
 */
public record ProntuarioCompleto(
        Prontuario prontuario,
        List<Alergia> alergias,
        List<MedicamentoEmUso> medicamentosAtivos,
        List<RegistroAtendimento> atendimentos,
        List<Exame> exames
) {}
