package com.sussmartassistant.assistente.application;

import com.sussmartassistant.assistente.domain.HipoteseDiagnostica;
import com.sussmartassistant.assistente.domain.SolicitacaoIA;
import com.sussmartassistant.paciente.application.AlergiaRepository;
import com.sussmartassistant.paciente.application.MedicamentoRepository;
import com.sussmartassistant.paciente.domain.Alergia;
import com.sussmartassistant.paciente.domain.MedicamentoEmUso;
import com.sussmartassistant.prontuario.application.*;
import com.sussmartassistant.prontuario.domain.Exame;
import com.sussmartassistant.prontuario.domain.Prontuario;
import com.sussmartassistant.prontuario.domain.RegistroAtendimento;
import com.sussmartassistant.shared.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class AssistenteUseCasesTest {

    private SolicitacaoIARepository solicitacaoRepository;
    private SolicitarHipotesesDiagnosticasUseCase solicitarHipoteses;
    private ConsultarResultadoIAUseCase consultarResultado;
    private ProcessarSolicitacaoIAUseCase processarSolicitacao;

    private UUID pacienteId;
    private UUID profissionalId;

    @BeforeEach
    void setUp() {
        pacienteId = UUID.randomUUID();
        profissionalId = UUID.randomUUID();

        Map<UUID, SolicitacaoIA> store = new HashMap<>();

        solicitacaoRepository = new SolicitacaoIARepository() {
            @Override
            public SolicitacaoIA salvar(SolicitacaoIA s) {
                store.put(s.getId(), s);
                return s;
            }

            @Override
            public Optional<SolicitacaoIA> buscarPorId(UUID id) {
                return Optional.ofNullable(store.get(id));
            }
        };

        List<UUID> published = new ArrayList<>();
        SolicitacaoIAPublisher publisher = published::add;

        solicitarHipoteses = new SolicitarHipotesesDiagnosticasUseCase(solicitacaoRepository, publisher);
        consultarResultado = new ConsultarResultadoIAUseCase(solicitacaoRepository);

        // Setup ProcessarSolicitacaoIAUseCase with mock dependencies
        UUID prontuarioId = UUID.randomUUID();
        Prontuario prontuario = new Prontuario(prontuarioId, pacienteId, Instant.now(), Instant.now());

        RegistroAtendimento atendimentoAnterior = RegistroAtendimento.criar(
                prontuarioId, profissionalId, UUID.randomUUID(),
                LocalDate.of(2024, 1, 1), "Febre", "Anamnese", "J06", "Prescrição", "Obs");

        Alergia alergia = Alergia.criar(pacienteId, "Penicilina", GravidadeAlergia.CRITICA,
                "Choque anafilático", profissionalId);

        ProntuarioCompleto prontuarioCompleto = new ProntuarioCompleto(
                prontuario, List.of(alergia), List.of(),
                List.of(atendimentoAnterior), List.of());

        ConsultarProntuarioCompletoUseCase consultarProntuarioUC = buildConsultarProntuario(prontuarioCompleto);

        PromptBuilder promptBuilder = (dados, sintomas) -> "prompt mock";

        String mockJsonResponse = """
                [
                  {"codigoCid": "J06", "justificativa": "IVAS recorrente", "confianca": "ALTA"},
                  {"codigoCid": "J03", "justificativa": "Amigdalite", "confianca": "MEDIA"}
                ]""";
        LlmGateway llmGateway = request -> new LlmResponse(mockJsonResponse);

        processarSolicitacao = new ProcessarSolicitacaoIAUseCase(
                solicitacaoRepository, consultarProntuarioUC, promptBuilder, llmGateway, "test-model");
    }

    @Test
    void deveSolicitarHipotesesComSucesso() {
        UUID id = solicitarHipoteses.executar(pacienteId, List.of("febre", "dor de garganta"), profissionalId);

        assertNotNull(id);
        SolicitacaoIA solicitacao = solicitacaoRepository.buscarPorId(id).orElseThrow();
        assertEquals(StatusSolicitacaoIA.PENDENTE, solicitacao.getStatus());
        assertEquals("febre; dor de garganta", solicitacao.getSintomasInformados());
    }

    @Test
    void deveRejeitarSolicitacaoSemSintomas() {
        assertThrows(ValidationException.class, () ->
                solicitarHipoteses.executar(pacienteId, List.of(), profissionalId));
    }

    @Test
    void deveRejeitarSolicitacaoComSintomasVazios() {
        assertThrows(ValidationException.class, () ->
                solicitarHipoteses.executar(pacienteId, List.of("", "  "), profissionalId));
    }

    @Test
    void deveConsultarResultadoExistente() {
        UUID id = solicitarHipoteses.executar(pacienteId, List.of("febre"), profissionalId);

        SolicitacaoIA resultado = consultarResultado.executar(id);

        assertEquals(id, resultado.getId());
    }

    @Test
    void deveRejeitarConsultaResultadoInexistente() {
        assertThrows(ResourceNotFoundException.class, () ->
                consultarResultado.executar(UUID.randomUUID()));
    }

    @Test
    void deveProcessarSolicitacaoComSucesso() {
        UUID id = solicitarHipoteses.executar(pacienteId, List.of("febre", "dor de garganta"), profissionalId);

        processarSolicitacao.executar(id);

        SolicitacaoIA resultado = solicitacaoRepository.buscarPorId(id).orElseThrow();
        assertEquals(StatusSolicitacaoIA.CONCLUIDA, resultado.getStatus());
        assertNotNull(resultado.getConcluidoEm());
        assertEquals(2, resultado.getHipoteses().size());
    }

    @Test
    void deveDetectarReincidenciaDeCid() {
        UUID id = solicitarHipoteses.executar(pacienteId, List.of("febre"), profissionalId);
        processarSolicitacao.executar(id);

        SolicitacaoIA resultado = solicitacaoRepository.buscarPorId(id).orElseThrow();
        HipoteseDiagnostica hipoteseJ06 = resultado.getHipoteses().stream()
                .filter(h -> "J06".equals(h.getCodigoCid())).findFirst().orElseThrow();

        assertTrue(hipoteseJ06.isReincidencia());
    }

    @Test
    void deveIncluirAlertaDeContraindicacao() {
        UUID id = solicitarHipoteses.executar(pacienteId, List.of("febre"), profissionalId);
        processarSolicitacao.executar(id);

        SolicitacaoIA resultado = solicitacaoRepository.buscarPorId(id).orElseThrow();
        for (HipoteseDiagnostica h : resultado.getHipoteses()) {
            assertNotNull(h.getAlertaContraindicacao());
            assertTrue(h.getAlertaContraindicacao().contains("Penicilina"));
        }
    }

    @Test
    void deveMarcarErroQuandoLlmFalha() {
        LlmGateway failingGateway = request -> {
            throw new LlmUnavailableException("Ollama offline");
        };
        ProcessarSolicitacaoIAUseCase useCaseComFalha = new ProcessarSolicitacaoIAUseCase(
                solicitacaoRepository, buildConsultarProntuario(
                new ProntuarioCompleto(
                        new Prontuario(UUID.randomUUID(), pacienteId, Instant.now(), Instant.now()),
                        List.of(), List.of(), List.of(), List.of())),
                (dados, sintomas) -> "prompt", failingGateway, "model");

        UUID id = solicitarHipoteses.executar(pacienteId, List.of("febre"), profissionalId);
        useCaseComFalha.executar(id);

        SolicitacaoIA resultado = solicitacaoRepository.buscarPorId(id).orElseThrow();
        assertEquals(StatusSolicitacaoIA.ERRO, resultado.getStatus());
        assertTrue(resultado.getRespostaLlm().contains("indisponível"));
    }

    private ConsultarProntuarioCompletoUseCase buildConsultarProntuario(ProntuarioCompleto completo) {
        ProntuarioRepository prontuarioRepo = new ProntuarioRepository() {
            @Override
            public Prontuario salvar(Prontuario p) { return p; }

            @Override
            public Optional<Prontuario> buscarPorPacienteId(UUID pid) {
                return pid.equals(pacienteId) ? Optional.of(completo.prontuario()) : Optional.empty();
            }
        };

        AlergiaRepository alergiaRepo = new AlergiaRepository() {
            @Override
            public Alergia salvar(Alergia a) { return a; }

            @Override
            public List<Alergia> buscarPorPacienteId(UUID pid) { return completo.alergias(); }

            @Override
            public boolean existePorPacienteESubstancia(UUID pid, String s) { return false; }
        };

        MedicamentoRepository medRepo = new MedicamentoRepository() {
            @Override
            public MedicamentoEmUso salvar(MedicamentoEmUso m) { return m; }

            @Override
            public Optional<MedicamentoEmUso> buscarPorId(UUID id) { return Optional.empty(); }

            @Override
            public List<MedicamentoEmUso> buscarAtivosPorPacienteId(UUID pid) { return completo.medicamentosAtivos(); }

            @Override
            public boolean existeAtivoPorPacienteNomeDosagem(UUID pid, String n, String d) { return false; }
        };

        AtendimentoRepository atendimentoRepo = new AtendimentoRepository() {
            @Override
            public RegistroAtendimento salvar(RegistroAtendimento a) { return a; }

            @Override
            public PageResult<RegistroAtendimento> buscarPorPacienteId(UUID pid, int page, int size) {
                return PageResult.of(completo.atendimentos(), page, size, completo.atendimentos().size());
            }

            @Override
            public PageResult<RegistroAtendimento> buscarPorPacienteIdEPeriodo(UUID pid, LocalDate i, LocalDate f, int page, int size) {
                return PageResult.of(List.of(), page, size, 0);
            }

            @Override
            public PageResult<RegistroAtendimento> buscarPorPacienteIdECid(UUID pid, String cid, int page, int size) {
                return PageResult.of(List.of(), page, size, 0);
            }
        };

        ExameRepository exameRepo = new ExameRepository() {
            @Override
            public Exame salvar(Exame e) { return e; }

            @Override
            public PageResult<Exame> buscarPorPacienteId(UUID pid, int page, int size) {
                return PageResult.of(completo.exames(), page, size, completo.exames().size());
            }

            @Override
            public PageResult<Exame> buscarPorPacienteIdETipo(UUID pid, String tipo, int page, int size) {
                return PageResult.of(List.of(), page, size, 0);
            }

            @Override
            public PageResult<Exame> buscarPorPacienteIdEPeriodo(UUID pid, LocalDate i, LocalDate f, int page, int size) {
                return PageResult.of(List.of(), page, size, 0);
            }
        };

        return new ConsultarProntuarioCompletoUseCase(prontuarioRepo, alergiaRepo, medRepo, atendimentoRepo, exameRepo);
    }
}
