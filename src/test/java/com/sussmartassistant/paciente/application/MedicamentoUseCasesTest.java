package com.sussmartassistant.paciente.application;

import com.sussmartassistant.paciente.domain.MedicamentoEmUso;
import com.sussmartassistant.paciente.domain.Paciente;
import com.sussmartassistant.shared.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class MedicamentoUseCasesTest {

    private MedicamentoRepository medicamentoRepository;
    private PacienteRepository pacienteRepository;
    private RegistrarMedicamentoUseCase registrarMedicamento;
    private DescontinuarMedicamentoUseCase descontinuarMedicamento;
    private ListarMedicamentosAtivosUseCase listarMedicamentos;

    private UUID pacienteId;
    private UUID profissionalId;

    @BeforeEach
    void setUp() {
        pacienteId = UUID.randomUUID();
        profissionalId = UUID.randomUUID();

        Paciente paciente = new Paciente(pacienteId, "João", new CPF("52998224725"),
                new CNS("198765432100001"), LocalDate.of(1990, 1, 1), "M", "tel",
                Instant.now(), Instant.now());

        Map<UUID, MedicamentoEmUso> medicamentos = new HashMap<>();

        medicamentoRepository = new MedicamentoRepository() {
            @Override
            public MedicamentoEmUso salvar(MedicamentoEmUso m) {
                medicamentos.put(m.getId(), m);
                return m;
            }

            @Override
            public Optional<MedicamentoEmUso> buscarPorId(UUID id) {
                return Optional.ofNullable(medicamentos.get(id));
            }

            @Override
            public List<MedicamentoEmUso> buscarAtivosPorPacienteId(UUID pid) {
                return medicamentos.values().stream()
                        .filter(m -> m.getPacienteId().equals(pid) && m.isAtivo()).toList();
            }

            @Override
            public boolean existeAtivoPorPacienteNomeDosagem(UUID pid, String nome, String dosagem) {
                return medicamentos.values().stream().anyMatch(m ->
                        m.getPacienteId().equals(pid) && m.getNome().equalsIgnoreCase(nome)
                                && m.getDosagem().equalsIgnoreCase(dosagem) && m.isAtivo());
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

        registrarMedicamento = new RegistrarMedicamentoUseCase(medicamentoRepository, pacienteRepository);
        descontinuarMedicamento = new DescontinuarMedicamentoUseCase(medicamentoRepository);
        listarMedicamentos = new ListarMedicamentosAtivosUseCase(medicamentoRepository, pacienteRepository);
    }

    @Test
    void deveRegistrarMedicamentoComSucesso() {
        MedicamentoEmUso resultado = registrarMedicamento.executar(pacienteId, "Losartana", "50mg",
                "1x ao dia", LocalDate.of(2024, 1, 1), profissionalId);

        assertEquals("Losartana", resultado.getNome());
        assertTrue(resultado.isAtivo());
    }

    @Test
    void deveRejeitarMedicamentoDuplicadoAtivo() {
        registrarMedicamento.executar(pacienteId, "Losartana", "50mg",
                "1x ao dia", LocalDate.of(2024, 1, 1), profissionalId);

        assertThrows(DomainException.class, () ->
                registrarMedicamento.executar(pacienteId, "Losartana", "50mg",
                        "2x ao dia", LocalDate.of(2024, 6, 1), profissionalId));
    }

    @Test
    void deveDescontinuarMedicamento() {
        MedicamentoEmUso med = registrarMedicamento.executar(pacienteId, "Losartana", "50mg",
                "1x ao dia", LocalDate.of(2024, 1, 1), profissionalId);

        MedicamentoEmUso descontinuado = descontinuarMedicamento.executar(med.getId());

        assertFalse(descontinuado.isAtivo());
        assertNotNull(descontinuado.getDataDescontinuacao());
    }

    @Test
    void deveRejeitarDescontinuarMedicamentoJaDescontinuado() {
        MedicamentoEmUso med = registrarMedicamento.executar(pacienteId, "Losartana", "50mg",
                "1x ao dia", LocalDate.of(2024, 1, 1), profissionalId);
        descontinuarMedicamento.executar(med.getId());

        assertThrows(DomainException.class, () -> descontinuarMedicamento.executar(med.getId()));
    }

    @Test
    void deveListarApenasMedicamentosAtivos() {
        MedicamentoEmUso med1 = registrarMedicamento.executar(pacienteId, "Losartana", "50mg",
                "1x ao dia", LocalDate.of(2024, 1, 1), profissionalId);
        registrarMedicamento.executar(pacienteId, "Omeprazol", "20mg",
                "1x ao dia", LocalDate.of(2024, 2, 1), profissionalId);
        descontinuarMedicamento.executar(med1.getId());

        List<MedicamentoEmUso> ativos = listarMedicamentos.executar(pacienteId);

        assertEquals(1, ativos.size());
        assertEquals("Omeprazol", ativos.get(0).getNome());
    }
}
