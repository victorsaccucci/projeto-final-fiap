package com.sussmartassistant.paciente.infrastructure;

import com.sussmartassistant.paciente.application.*;
import com.sussmartassistant.prontuario.application.ProntuarioRepository;
import com.sussmartassistant.prontuario.domain.Prontuario;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
public class PacienteModuleConfig {

    @Bean
    public PacienteCadastradoCallback pacienteCadastradoCallback(ProntuarioRepository prontuarioRepository) {
        return new PacienteCadastradoCallback() {
            @Override
            @Transactional
            public void onPacienteCadastrado(java.util.UUID pacienteId) {
                prontuarioRepository.salvar(Prontuario.criar(pacienteId));
            }
        };
    }

    @Bean
    public CadastrarPacienteUseCase cadastrarPacienteUseCase(PacienteRepository pacienteRepository,
                                                              PacienteCadastradoCallback callback) {
        return new CadastrarPacienteUseCase(pacienteRepository, callback);
    }

    @Bean
    public BuscarPacientePorCpfUseCase buscarPacientePorCpfUseCase(PacienteRepository pacienteRepository) {
        return new BuscarPacientePorCpfUseCase(pacienteRepository);
    }

    @Bean
    public RegistrarAlergiaUseCase registrarAlergiaUseCase(AlergiaRepository alergiaRepository,
                                                            PacienteRepository pacienteRepository) {
        return new RegistrarAlergiaUseCase(alergiaRepository, pacienteRepository);
    }

    @Bean
    public ListarAlergiasUseCase listarAlergiasUseCase(AlergiaRepository alergiaRepository,
                                                        PacienteRepository pacienteRepository) {
        return new ListarAlergiasUseCase(alergiaRepository, pacienteRepository);
    }

    @Bean
    public RegistrarMedicamentoUseCase registrarMedicamentoUseCase(MedicamentoRepository medicamentoRepository,
                                                                    PacienteRepository pacienteRepository) {
        return new RegistrarMedicamentoUseCase(medicamentoRepository, pacienteRepository);
    }

    @Bean
    public ListarMedicamentosAtivosUseCase listarMedicamentosAtivosUseCase(
            MedicamentoRepository medicamentoRepository, PacienteRepository pacienteRepository) {
        return new ListarMedicamentosAtivosUseCase(medicamentoRepository, pacienteRepository);
    }

    @Bean
    public DescontinuarMedicamentoUseCase descontinuarMedicamentoUseCase(
            MedicamentoRepository medicamentoRepository) {
        return new DescontinuarMedicamentoUseCase(medicamentoRepository);
    }
}
