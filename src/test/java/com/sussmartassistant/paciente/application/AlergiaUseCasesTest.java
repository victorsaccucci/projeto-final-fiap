package com.sussmartassistant.paciente.application;

import com.sussmartassistant.paciente.domain.Alergia;
import com.sussmartassistant.paciente.domain.Paciente;
import com.sussmartassistant.shared.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class AlergiaUseCasesTest {

    private AlergiaRepository alergiaRepository;
    private PacienteRepository pacienteRepository;
    private RegistrarAlergiaUseCase registrarAlergia;
    private ListarAlergiasUseCase listarAlergias;

    private UUID pacienteId;
    private UUID profissionalId;

    @BeforeEach
    void setUp() {
        pacienteId = UUID.randomUUID();
        profissionalId = UUID.randomUUID();

        Paciente paciente = new Paciente(pacienteId, "Maria", new CPF("52998224725"),
                new CNS("198765432100001"), LocalDate.of(1985, 3, 15), "F", "tel",
                Instant.now(), Instant.now());

        List<Alergia> alergias = new ArrayList<>();

        alergiaRepository = new AlergiaRepository() {
            @Override
            public Alergia salvar(Alergia alergia) {
                alergias.add(alergia);
                return alergia;
            }

            @Override
            public List<Alergia> buscarPorPacienteId(UUID pid) {
                return alergias.stream().filter(a -> a.getPacienteId().equals(pid)).toList();
            }

            @Override
            public boolean existePorPacienteESubstancia(UUID pid, String substancia) {
                return alergias.stream().anyMatch(a ->
                        a.getPacienteId().equals(pid) && a.getSubstancia().equalsIgnoreCase(substancia));
            }
        };

        pacienteRepository = new PacienteRepository() {
            @Override
            public Paciente salvar(Paciente p) { return p; }

            @Override
            public Optional<Paciente> buscarPorCpf(CPF cpf) { return Optional.empty(); }

            @Override
            public Optional<Paciente> buscarPorId(UUID id) {
                return id.equals(pacienteId) ? Optional.of(paciente) : Optional.empty();
            }

            @Override
            public boolean existePorCpf(CPF cpf) { return false; }
        };

        registrarAlergia = new RegistrarAlergiaUseCase(alergiaRepository, pacienteRepository);
        listarAlergias = new ListarAlergiasUseCase(alergiaRepository, pacienteRepository);
    }

    @Test
    void deveRegistrarAlergiaComSucesso() {
        Alergia resultado = registrarAlergia.executar(pacienteId, "Penicilina",
                GravidadeAlergia.CRITICA, "Choque anafilático", profissionalId);

        assertEquals("Penicilina", resultado.getSubstancia());
        assertEquals(GravidadeAlergia.CRITICA, resultado.getGravidade());
    }

    @Test
    void deveRejeitarAlergiaDuplicada() {
        registrarAlergia.executar(pacienteId, "Penicilina",
                GravidadeAlergia.CRITICA, "Choque", profissionalId);

        assertThrows(DomainException.class, () ->
                registrarAlergia.executar(pacienteId, "Penicilina",
                        GravidadeAlergia.LEVE, "Outra reação", profissionalId));
    }

    @Test
    void deveRejeitarAlergiaPacienteInexistente() {
        assertThrows(ResourceNotFoundException.class, () ->
                registrarAlergia.executar(UUID.randomUUID(), "Dipirona",
                        GravidadeAlergia.LEVE, "Urticária", profissionalId));
    }

    @Test
    void deveListarAlergiasOrdenadasPorGravidade() {
        registrarAlergia.executar(pacienteId, "Látex", GravidadeAlergia.LEVE, "Dermatite", profissionalId);
        registrarAlergia.executar(pacienteId, "Penicilina", GravidadeAlergia.CRITICA, "Choque", profissionalId);
        registrarAlergia.executar(pacienteId, "Dipirona", GravidadeAlergia.MODERADA, "Urticária", profissionalId);

        List<Alergia> resultado = listarAlergias.executar(pacienteId);

        assertEquals(3, resultado.size());
        assertEquals(GravidadeAlergia.CRITICA, resultado.get(0).getGravidade());
        assertEquals(GravidadeAlergia.MODERADA, resultado.get(1).getGravidade());
        assertEquals(GravidadeAlergia.LEVE, resultado.get(2).getGravidade());
    }
}
