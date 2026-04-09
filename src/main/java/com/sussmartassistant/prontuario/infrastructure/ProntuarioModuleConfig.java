package com.sussmartassistant.prontuario.infrastructure;

import com.sussmartassistant.paciente.application.AlergiaRepository;
import com.sussmartassistant.paciente.application.MedicamentoRepository;
import com.sussmartassistant.prontuario.application.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProntuarioModuleConfig {

    @Bean
    public ConsultarProntuarioCompletoUseCase consultarProntuarioCompletoUseCase(
            ProntuarioRepository prontuarioRepository,
            AlergiaRepository alergiaRepository,
            MedicamentoRepository medicamentoRepository,
            AtendimentoRepository atendimentoRepository,
            ExameRepository exameRepository) {
        return new ConsultarProntuarioCompletoUseCase(prontuarioRepository, alergiaRepository,
                medicamentoRepository, atendimentoRepository, exameRepository);
    }

    @Bean
    public RegistrarAtendimentoUseCase registrarAtendimentoUseCase(
            ProntuarioRepository prontuarioRepository,
            AtendimentoRepository atendimentoRepository) {
        return new RegistrarAtendimentoUseCase(prontuarioRepository, atendimentoRepository);
    }

    @Bean
    public ListarAtendimentosUseCase listarAtendimentosUseCase(
            ProntuarioRepository prontuarioRepository,
            AtendimentoRepository atendimentoRepository) {
        return new ListarAtendimentosUseCase(prontuarioRepository, atendimentoRepository);
    }

    @Bean
    public RegistrarExameUseCase registrarExameUseCase(
            ProntuarioRepository prontuarioRepository,
            ExameRepository exameRepository) {
        return new RegistrarExameUseCase(prontuarioRepository, exameRepository);
    }

    @Bean
    public ListarExamesUseCase listarExamesUseCase(
            ProntuarioRepository prontuarioRepository,
            ExameRepository exameRepository) {
        return new ListarExamesUseCase(prontuarioRepository, exameRepository);
    }
}
