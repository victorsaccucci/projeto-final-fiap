package com.sussmartassistant.prontuario.application;

import com.sussmartassistant.paciente.application.AlergiaRepository;
import com.sussmartassistant.paciente.application.MedicamentoRepository;
import com.sussmartassistant.paciente.domain.Alergia;
import com.sussmartassistant.paciente.domain.MedicamentoEmUso;
import com.sussmartassistant.prontuario.domain.Exame;
import com.sussmartassistant.prontuario.domain.Prontuario;
import com.sussmartassistant.prontuario.domain.RegistroAtendimento;
import com.sussmartassistant.shared.domain.ResourceNotFoundException;

import java.util.List;
import java.util.UUID;

public class ConsultarProntuarioCompletoUseCase {

    private final ProntuarioRepository prontuarioRepository;
    private final AlergiaRepository alergiaRepository;
    private final MedicamentoRepository medicamentoRepository;
    private final AtendimentoRepository atendimentoRepository;
    private final ExameRepository exameRepository;

    public ConsultarProntuarioCompletoUseCase(ProntuarioRepository prontuarioRepository,
                                               AlergiaRepository alergiaRepository,
                                               MedicamentoRepository medicamentoRepository,
                                               AtendimentoRepository atendimentoRepository,
                                               ExameRepository exameRepository) {
        this.prontuarioRepository = prontuarioRepository;
        this.alergiaRepository = alergiaRepository;
        this.medicamentoRepository = medicamentoRepository;
        this.atendimentoRepository = atendimentoRepository;
        this.exameRepository = exameRepository;
    }

    public ProntuarioCompleto executar(UUID pacienteId) {
        Prontuario prontuario = prontuarioRepository.buscarPorPacienteId(pacienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Prontuário não encontrado para paciente: " + pacienteId));

        return montarCompleto(pacienteId, prontuario);
    }

    public ProntuarioCompleto executarOuVazio(UUID pacienteId) {
        Prontuario prontuario = prontuarioRepository.buscarPorPacienteId(pacienteId)
                .orElse(Prontuario.criar(pacienteId));

        return montarCompleto(pacienteId, prontuario);
    }

    private ProntuarioCompleto montarCompleto(UUID pacienteId, Prontuario prontuario) {
        List<Alergia> alergias = alergiaRepository.buscarPorPacienteId(pacienteId);
        List<MedicamentoEmUso> medicamentos = medicamentoRepository.buscarAtivosPorPacienteId(pacienteId);
        List<RegistroAtendimento> atendimentos = atendimentoRepository
                .buscarPorPacienteId(pacienteId, 0, 20).content();
        List<Exame> exames = exameRepository
                .buscarPorPacienteId(pacienteId, 0, 20).content();

        return new ProntuarioCompleto(prontuario, alergias, medicamentos, atendimentos, exames);
    }
}
