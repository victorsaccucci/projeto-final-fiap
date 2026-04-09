package com.sussmartassistant.prontuario.application;

import com.sussmartassistant.paciente.application.AlergiaRepository;
import com.sussmartassistant.paciente.application.MedicamentoRepository;
import com.sussmartassistant.paciente.domain.Alergia;
import com.sussmartassistant.paciente.domain.MedicamentoEmUso;
import com.sussmartassistant.prontuario.domain.Exame;
import com.sussmartassistant.prontuario.domain.Prontuario;
import com.sussmartassistant.prontuario.domain.RegistroAtendimento;
import com.sussmartassistant.shared.domain.PageResult;
import com.sussmartassistant.shared.domain.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ProntuarioUseCasesTest {

    private UUID pacienteId;
    private UUID prontuarioId;
    private UUID profissionalId;
    private UUID unidadeId;

    private ProntuarioRepository prontuarioRepository;
    private AtendimentoRepository atendimentoRepository;
    private ExameRepository exameRepository;

    private RegistrarAtendimentoUseCase registrarAtendimento;
    private RegistrarExameUseCase registrarExame;
    private ConsultarProntuarioCompletoUseCase consultarProntuario;

    @BeforeEach
    void setUp() {
        pacienteId = UUID.randomUUID();
        prontuarioId = UUID.randomUUID();
        profissionalId = UUID.randomUUID();
        unidadeId = UUID.randomUUID();

        Prontuario prontuario = new Prontuario(prontuarioId, pacienteId,
                java.time.Instant.now(), java.time.Instant.now());

        List<RegistroAtendimento> atendimentos = new ArrayList<>();
        List<Exame> exames = new ArrayList<>();

        prontuarioRepository = new ProntuarioRepository() {
            @Override
            public Prontuario salvar(Prontuario p) { return p; }

            @Override
            public Optional<Prontuario> buscarPorPacienteId(UUID pid) {
                return pid.equals(pacienteId) ? Optional.of(prontuario) : Optional.empty();
            }
        };

        atendimentoRepository = new AtendimentoRepository() {
            @Override
            public RegistroAtendimento salvar(RegistroAtendimento a) {
                atendimentos.add(a);
                return a;
            }

            @Override
            public PageResult<RegistroAtendimento> buscarPorPacienteId(UUID pid, int page, int size) {
                var filtered = atendimentos.stream()
                        .filter(a -> a.getProntuarioId().equals(prontuarioId)).toList();
                return PageResult.of(filtered, page, size, filtered.size());
            }

            @Override
            public PageResult<RegistroAtendimento> buscarPorPacienteIdEPeriodo(UUID pid, LocalDate inicio, LocalDate fim, int page, int size) {
                var filtered = atendimentos.stream()
                        .filter(a -> !a.getData().isBefore(inicio) && !a.getData().isAfter(fim)).toList();
                return PageResult.of(filtered, page, size, filtered.size());
            }

            @Override
            public PageResult<RegistroAtendimento> buscarPorPacienteIdECid(UUID pid, String cid, int page, int size) {
                var filtered = atendimentos.stream()
                        .filter(a -> cid.equals(a.getDiagnosticoCid())).toList();
                return PageResult.of(filtered, page, size, filtered.size());
            }
        };

        exameRepository = new ExameRepository() {
            @Override
            public Exame salvar(Exame e) {
                exames.add(e);
                return e;
            }

            @Override
            public PageResult<Exame> buscarPorPacienteId(UUID pid, int page, int size) {
                return PageResult.of(exames, page, size, exames.size());
            }

            @Override
            public PageResult<Exame> buscarPorPacienteIdETipo(UUID pid, String tipo, int page, int size) {
                var filtered = exames.stream().filter(e -> tipo.equalsIgnoreCase(e.getTipo())).toList();
                return PageResult.of(filtered, page, size, filtered.size());
            }

            @Override
            public PageResult<Exame> buscarPorPacienteIdEPeriodo(UUID pid, LocalDate inicio, LocalDate fim, int page, int size) {
                return PageResult.of(exames, page, size, exames.size());
            }
        };

        AlergiaRepository alergiaRepo = new AlergiaRepository() {
            @Override
            public Alergia salvar(Alergia a) { return a; }

            @Override
            public List<Alergia> buscarPorPacienteId(UUID pid) { return List.of(); }

            @Override
            public boolean existePorPacienteESubstancia(UUID pid, String s) { return false; }
        };

        MedicamentoRepository medRepo = new MedicamentoRepository() {
            @Override
            public MedicamentoEmUso salvar(MedicamentoEmUso m) { return m; }

            @Override
            public Optional<MedicamentoEmUso> buscarPorId(UUID id) { return Optional.empty(); }

            @Override
            public List<MedicamentoEmUso> buscarAtivosPorPacienteId(UUID pid) { return List.of(); }

            @Override
            public boolean existeAtivoPorPacienteNomeDosagem(UUID pid, String n, String d) { return false; }
        };

        registrarAtendimento = new RegistrarAtendimentoUseCase(prontuarioRepository, atendimentoRepository);
        registrarExame = new RegistrarExameUseCase(prontuarioRepository, exameRepository);
        consultarProntuario = new ConsultarProntuarioCompletoUseCase(
                prontuarioRepository, alergiaRepo, medRepo, atendimentoRepository, exameRepository);
    }

    @Test
    void deveRegistrarAtendimentoComSucesso() {
        RegistroAtendimento resultado = registrarAtendimento.executar(pacienteId, profissionalId, unidadeId,
                LocalDate.of(2024, 6, 1), "Dor de cabeça", "Cefaleia frontal",
                "R51", "Paracetamol 750mg", "Retorno se persistir");

        assertEquals(prontuarioId, resultado.getProntuarioId());
        assertEquals("R51", resultado.getDiagnosticoCid());
    }

    @Test
    void deveRejeitarAtendimentoPacienteSemProntuario() {
        assertThrows(ResourceNotFoundException.class, () ->
                registrarAtendimento.executar(UUID.randomUUID(), profissionalId, unidadeId,
                        LocalDate.now(), "Queixa", "Anamnese", "R51", "Prescrição", "Obs"));
    }

    @Test
    void deveRegistrarExameComSucesso() {
        Exame resultado = registrarExame.executar(pacienteId, "Hemograma",
                LocalDate.of(2024, 6, 1), "Normal", unidadeId, profissionalId);

        assertEquals("Hemograma", resultado.getTipo());
        assertEquals(prontuarioId, resultado.getProntuarioId());
    }

    @Test
    void deveConsultarProntuarioCompleto() {
        registrarAtendimento.executar(pacienteId, profissionalId, unidadeId,
                LocalDate.of(2024, 6, 1), "Queixa", "Anamnese", "J06", "Prescrição", "Obs");
        registrarExame.executar(pacienteId, "Hemograma", LocalDate.of(2024, 6, 1),
                "Normal", unidadeId, profissionalId);

        ProntuarioCompleto completo = consultarProntuario.executar(pacienteId);

        assertNotNull(completo.prontuario());
        assertEquals(1, completo.atendimentos().size());
        assertEquals(1, completo.exames().size());
        assertEquals(0, completo.alergias().size());
        assertEquals(0, completo.medicamentosAtivos().size());
    }

    @Test
    void deveRejeitarConsultaProntuarioInexistente() {
        assertThrows(ResourceNotFoundException.class, () ->
                consultarProntuario.executar(UUID.randomUUID()));
    }
}
