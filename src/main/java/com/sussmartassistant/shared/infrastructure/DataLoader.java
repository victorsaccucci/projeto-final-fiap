package com.sussmartassistant.shared.infrastructure;

import com.sussmartassistant.paciente.infrastructure.*;
import com.sussmartassistant.prontuario.infrastructure.*;
import com.sussmartassistant.seguranca.infrastructure.JpaUsuarioRepository;
import com.sussmartassistant.seguranca.infrastructure.UsuarioEntity;
import com.sussmartassistant.seguranca.domain.Usuario;
import com.sussmartassistant.shared.domain.*;
import com.sussmartassistant.paciente.domain.*;
import com.sussmartassistant.prontuario.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

/**
 * Carrega dados mockados realistas no profile dev.
 * Idempotente: verifica existência antes de inserir.
 */
@Component
@Profile("dev")
public class DataLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataLoader.class);

    private final JpaUnidadeSaudeRepository unidadeRepo;
    private final JpaProfissionalSaudeRepository profissionalRepo;
    private final JpaPacienteRepository pacienteRepo;
    private final JpaProntuarioRepository prontuarioRepo;
    private final JpaAlergiaRepository alergiaRepo;
    private final JpaMedicamentoRepository medicamentoRepo;
    private final JpaAtendimentoRepository atendimentoRepo;
    private final JpaExameRepository exameRepo;
    private final JpaUsuarioRepository usuarioRepo;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(JpaUnidadeSaudeRepository unidadeRepo,
                      JpaProfissionalSaudeRepository profissionalRepo,
                      JpaPacienteRepository pacienteRepo,
                      JpaProntuarioRepository prontuarioRepo,
                      JpaAlergiaRepository alergiaRepo,
                      JpaMedicamentoRepository medicamentoRepo,
                      JpaAtendimentoRepository atendimentoRepo,
                      JpaExameRepository exameRepo,
                      JpaUsuarioRepository usuarioRepo,
                      PasswordEncoder passwordEncoder) {
        this.unidadeRepo = unidadeRepo;
        this.profissionalRepo = profissionalRepo;
        this.pacienteRepo = pacienteRepo;
        this.prontuarioRepo = prontuarioRepo;
        this.alergiaRepo = alergiaRepo;
        this.medicamentoRepo = medicamentoRepo;
        this.atendimentoRepo = atendimentoRepo;
        this.exameRepo = exameRepo;
        this.usuarioRepo = usuarioRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (pacienteRepo.count() > 0) {
            log.info("Dados mockados já existem, pulando carregamento.");
            return;
        }
        log.info("Carregando dados mockados para ambiente de desenvolvimento...");

        // 1. Unidades de Saúde
        var unidades = criarUnidadesSaude();
        // 2. Profissionais de Saúde
        var profissionais = criarProfissionais(unidades);
        // 3. Pacientes com prontuários completos
        var pacientes = criarPacientes(unidades, profissionais);
        // 4. Usuários
        criarUsuarios(profissionais, pacientes);

        log.info("Dados mockados carregados com sucesso: {} unidades, {} profissionais, {} pacientes",
                unidades.size(), profissionais.size(), pacientes.size());
    }

    // ==================== UNIDADES DE SAÚDE ====================

    private List<UnidadeSaudeEntity> criarUnidadesSaude() {
        var hospital = salvarUnidade("Hospital Municipal São Lucas", "2077485",
                "Rua das Flores, 100 - Centro, São Paulo/SP", TipoUnidadeSaude.HOSPITAL,
                List.of("Clínica Geral", "Cardiologia", "Ortopedia", "Pediatria", "Emergência"));

        var ubs = salvarUnidade("UBS Vila Esperança", "3150430",
                "Av. Brasil, 500 - Vila Esperança, São Paulo/SP", TipoUnidadeSaude.UBS,
                List.of("Clínica Geral", "Pediatria", "Ginecologia", "Enfermagem"));

        var clinica = salvarUnidade("Clínica Saúde Integral", "6420532",
                "Rua Augusta, 250 - Consolação, São Paulo/SP", TipoUnidadeSaude.CLINICA,
                List.of("Dermatologia", "Endocrinologia", "Clínica Geral"));

        return List.of(hospital, ubs, clinica);
    }

    private UnidadeSaudeEntity salvarUnidade(String nome, String cnes, String endereco,
                                              TipoUnidadeSaude tipo, List<String> especialidades) {
        var entity = UnidadeSaudeEntity.fromDomain(
                UnidadeSaude.criar(nome, cnes, endereco, tipo, especialidades));
        return unidadeRepo.save(entity);
    }

    // ==================== PROFISSIONAIS DE SAÚDE ====================

    private List<ProfissionalSaudeEntity> criarProfissionais(List<UnidadeSaudeEntity> unidades) {
        UUID hospitalId = unidades.get(0).getId();
        UUID ubsId = unidades.get(1).getId();
        UUID clinicaId = unidades.get(2).getId();

        var p1 = salvarProfissional("Dr. Carlos Eduardo Mendes", "CRM-SP-123456", "Clínica Geral", hospitalId);
        var p2 = salvarProfissional("Dra. Fernanda Oliveira", "CRM-SP-234567", "Cardiologia", hospitalId);
        var p3 = salvarProfissional("Dr. Roberto Almeida", "CRM-SP-345678", "Pediatria", ubsId);
        var p4 = salvarProfissional("Dra. Patrícia Lima", "CRM-SP-456789", "Ginecologia", ubsId);
        var p5 = salvarProfissional("Dr. André Souza", "CRM-SP-567890", "Dermatologia", clinicaId);

        return List.of(p1, p2, p3, p4, p5);
    }

    private ProfissionalSaudeEntity salvarProfissional(String nome, String crm, String especialidade, UUID unidadeId) {
        var entity = ProfissionalSaudeEntity.fromDomain(
                ProfissionalSaude.criar(nome, crm, especialidade, unidadeId));
        return profissionalRepo.save(entity);
    }

    // ==================== PACIENTES ====================

    // Valid CPFs (pass digit verification algorithm)
    private static final String[] CPFS_VALIDOS = {
        "52998224725", "11144477735", "98765432100",
        "71428793860", "89012345642", "45678912364",
        "32165498791", "65432178982", "78945612319",
        "14725836982", "12345678909"
    };

    // Valid CNS numbers (15 digits)
    private static final String[] CNS_VALIDOS = {
        "198765432100001", "198765432100002", "198765432100003",
        "198765432100004", "198765432100005", "198765432100006",
        "198765432100007", "198765432100008", "198765432100009",
        "198765432100010", "198765432100011"
    };

    private List<PacienteEntity> criarPacientes(List<UnidadeSaudeEntity> unidades,
                                                 List<ProfissionalSaudeEntity> profissionais) {
        UUID hospitalId = unidades.get(0).getId();
        UUID ubsId = unidades.get(1).getId();
        UUID clinicaId = unidades.get(2).getId();
        UUID drCarlosId = profissionais.get(0).getId();
        UUID draFernandaId = profissionais.get(1).getId();
        UUID drRobertoId = profissionais.get(2).getId();
        UUID draPatriciaId = profissionais.get(3).getId();
        UUID drAndreId = profissionais.get(4).getId();

        List<PacienteEntity> pacientes = new ArrayList<>();

        // (a) Maria Silva — DEMO patient: extensive history, reincidência J06, multiple alergias
        pacientes.add(criarMariaSilva(hospitalId, ubsId, drCarlosId, draFernandaId, drRobertoId));

        // (b) João Santos — minimal data
        pacientes.add(criarJoaoSantos(ubsId, drRobertoId));

        // (c) Ana Costa — alergias for contraindication alerts
        pacientes.add(criarAnaCosta(hospitalId, clinicaId, drCarlosId, drAndreId));

        // Additional patients (7 more to reach 10)
        pacientes.add(criarPacienteSimples("Pedro Ferreira", CPFS_VALIDOS[3], CNS_VALIDOS[3],
                LocalDate.of(1955, 3, 20), "M", hospitalId, draFernandaId,
                "Hipertensão arterial", "I10", "Losartana", "50mg"));

        pacientes.add(criarPacienteSimples("Lucia Rodrigues", CPFS_VALIDOS[4], CNS_VALIDOS[4],
                LocalDate.of(1990, 7, 8), "F", ubsId, draPatriciaId,
                "Infecção urinária", "N39", "Ciprofloxacino", "500mg"));

        pacientes.add(criarPacienteSimples("Francisco Barbosa", CPFS_VALIDOS[5], CNS_VALIDOS[5],
                LocalDate.of(1978, 11, 30), "M", hospitalId, drCarlosId,
                "Lombalgia", "M54", "Ibuprofeno", "600mg"));

        pacientes.add(criarPacienteSimples("Mariana Pereira", CPFS_VALIDOS[6], CNS_VALIDOS[6],
                LocalDate.of(2000, 1, 15), "F", clinicaId, drAndreId,
                "Dermatite atópica", "L20", "Hidrocortisona creme", "1%"));

        pacientes.add(criarPacienteSimples("José Oliveira", CPFS_VALIDOS[7], CNS_VALIDOS[7],
                LocalDate.of(1965, 5, 22), "M", hospitalId, draFernandaId,
                "Diabetes mellitus tipo 2", "E11", "Metformina", "850mg"));

        pacientes.add(criarPacienteSimples("Beatriz Souza", CPFS_VALIDOS[8], CNS_VALIDOS[8],
                LocalDate.of(1988, 9, 3), "F", ubsId, drRobertoId,
                "Asma", "J45", "Salbutamol", "100mcg"));

        pacientes.add(criarPacienteSimples("Ricardo Santos", CPFS_VALIDOS[9], CNS_VALIDOS[9],
                LocalDate.of(1972, 12, 10), "M", clinicaId, drCarlosId,
                "Gastrite", "K29", "Omeprazol", "20mg"));

        // (d) Camila Ferreira — asma, bronquite, rinite, sinusite, endometriose, alergia AAS
        pacientes.add(criarCamilaFerreira(hospitalId, ubsId, drCarlosId, draPatriciaId, drRobertoId));

        return pacientes;
    }

    // ==================== MARIA SILVA — Demo Patient (a) ====================

    private PacienteEntity criarMariaSilva(UUID hospitalId, UUID ubsId,
                                            UUID drCarlosId, UUID draFernandaId, UUID drRobertoId) {
        var paciente = salvarPaciente("Maria Silva", CPFS_VALIDOS[0], CNS_VALIDOS[0],
                LocalDate.of(1985, 3, 15), "F", "(11) 98765-4321");
        UUID pacienteId = paciente.getId();
        var prontuario = salvarProntuario(pacienteId);
        UUID prontuarioId = prontuario.getId();

        // Alergias: Penicilina CRITICA, Dipirona MODERADA, Látex LEVE
        salvarAlergia(pacienteId, "Penicilina", GravidadeAlergia.CRITICA,
                "Choque anafilático com edema de glote", drCarlosId);
        salvarAlergia(pacienteId, "Dipirona", GravidadeAlergia.MODERADA,
                "Urticária generalizada e broncoespasmo leve", drRobertoId);
        salvarAlergia(pacienteId, "Látex", GravidadeAlergia.LEVE,
                "Dermatite de contato localizada", drCarlosId);

        // Medicamentos ativos
        salvarMedicamento(pacienteId, "Losartana", "50mg", "1x ao dia", LocalDate.of(2023, 1, 10), drCarlosId);
        salvarMedicamento(pacienteId, "Sinvastatina", "20mg", "1x ao dia à noite", LocalDate.of(2023, 6, 15), draFernandaId);
        salvarMedicamento(pacienteId, "Omeprazol", "20mg", "1x ao dia em jejum", LocalDate.of(2024, 2, 1), drCarlosId);

        // Atendimentos — reincidência de J06 (IVAS)
        salvarAtendimento(prontuarioId, drCarlosId, hospitalId, LocalDate.of(2023, 3, 10),
                "Dor de garganta e febre", "Paciente refere odinofagia há 3 dias com febre de 38.5°C",
                "J06", "Amoxicilina 500mg 8/8h por 7 dias", "Repouso e hidratação");

        salvarAtendimento(prontuarioId, drRobertoId, ubsId, LocalDate.of(2023, 7, 22),
                "Tosse e congestão nasal", "Quadro de IVAS com tosse produtiva e rinorreia há 5 dias",
                "J06", "Azitromicina 500mg 1x/dia por 3 dias", "Reincidência de IVAS — acompanhar");

        salvarAtendimento(prontuarioId, draFernandaId, hospitalId, LocalDate.of(2023, 11, 5),
                "Dor torácica leve", "Dor precordial atípica, sem irradiação. PA 140/90",
                "I10", "Ajuste de Losartana para 50mg", "ECG normal. Manter acompanhamento cardiológico");

        salvarAtendimento(prontuarioId, drCarlosId, hospitalId, LocalDate.of(2024, 2, 14),
                "Dor epigástrica", "Queimação epigástrica pós-prandial há 2 semanas",
                "K21", "Omeprazol 20mg em jejum", "Solicitar endoscopia se persistir");

        salvarAtendimento(prontuarioId, drRobertoId, ubsId, LocalDate.of(2024, 6, 3),
                "Febre e dor de garganta", "Terceiro episódio de IVAS no último ano. Febre 38°C, odinofagia",
                "J06", "Claritromicina 500mg 12/12h por 7 dias",
                "REINCIDÊNCIA J06 — considerar investigação imunológica");

        salvarAtendimento(prontuarioId, draFernandaId, hospitalId, LocalDate.of(2024, 9, 18),
                "Consulta de rotina cardiológica", "PA controlada 130/85. Colesterol total 210",
                "Z00", "Manter Sinvastatina 20mg", "Solicitar perfil lipídico em 3 meses");

        // Exames
        salvarExame(prontuarioId, "Hemograma completo", LocalDate.of(2023, 3, 10),
                "Leucócitos: 12.000/mm³ (elevado), Hemoglobina: 13.5g/dL, Plaquetas: 250.000/mm³",
                hospitalId, drCarlosId);

        salvarExame(prontuarioId, "Perfil lipídico", LocalDate.of(2023, 11, 5),
                "Colesterol total: 220mg/dL, LDL: 140mg/dL, HDL: 45mg/dL, Triglicerídeos: 180mg/dL",
                hospitalId, draFernandaId);

        salvarExame(prontuarioId, "ECG", LocalDate.of(2023, 11, 5),
                "Ritmo sinusal, FC 78bpm, sem alterações isquêmicas",
                hospitalId, draFernandaId);

        salvarExame(prontuarioId, "Glicemia de jejum", LocalDate.of(2024, 2, 14),
                "Glicemia: 98mg/dL (normal)", hospitalId, drCarlosId);

        salvarExame(prontuarioId, "Hemograma completo", LocalDate.of(2024, 6, 3),
                "Leucócitos: 11.500/mm³ (levemente elevado), Hemoglobina: 13.2g/dL",
                ubsId, drRobertoId);

        return paciente;
    }

    // ==================== JOÃO SANTOS — Minimal Data (b) ====================

    private PacienteEntity criarJoaoSantos(UUID ubsId, UUID drRobertoId) {
        var paciente = salvarPaciente("João Santos", CPFS_VALIDOS[1], CNS_VALIDOS[1],
                LocalDate.of(1992, 8, 25), "M", "(11) 91234-5678");
        UUID pacienteId = paciente.getId();
        var prontuario = salvarProntuario(pacienteId);

        // Only one atendimento — minimal data for IA to request more info
        salvarAtendimento(prontuario.getId(), drRobertoId, ubsId, LocalDate.of(2024, 10, 1),
                "Cefaleia", "Paciente refere cefaleia frontal há 2 dias",
                "R51", "Paracetamol 750mg 6/6h se dor", "Retorno se persistir");

        return paciente;
    }

    // ==================== ANA COSTA — Alergias/Contraindication (c) ====================

    private PacienteEntity criarAnaCosta(UUID hospitalId, UUID clinicaId,
                                          UUID drCarlosId, UUID drAndreId) {
        var paciente = salvarPaciente("Ana Costa", CPFS_VALIDOS[2], CNS_VALIDOS[2],
                LocalDate.of(1978, 12, 5), "F", "(11) 95555-1234");
        UUID pacienteId = paciente.getId();
        var prontuario = salvarProntuario(pacienteId);
        UUID prontuarioId = prontuario.getId();

        // Multiple alergias — for contraindication demo
        salvarAlergia(pacienteId, "Sulfonamidas", GravidadeAlergia.CRITICA,
                "Síndrome de Stevens-Johnson", drCarlosId);
        salvarAlergia(pacienteId, "AINEs", GravidadeAlergia.MODERADA,
                "Broncoespasmo e edema facial", drCarlosId);
        salvarAlergia(pacienteId, "Contraste iodado", GravidadeAlergia.CRITICA,
                "Reação anafilactoide com hipotensão", drCarlosId);
        salvarAlergia(pacienteId, "Amoxicilina", GravidadeAlergia.MODERADA,
                "Exantema maculopapular difuso", drAndreId);

        // Medicamentos
        salvarMedicamento(pacienteId, "Prednisona", "5mg", "1x ao dia", LocalDate.of(2024, 1, 15), drCarlosId);
        salvarMedicamento(pacienteId, "Loratadina", "10mg", "1x ao dia", LocalDate.of(2024, 3, 1), drAndreId);

        // Atendimentos
        salvarAtendimento(prontuarioId, drCarlosId, hospitalId, LocalDate.of(2024, 1, 15),
                "Reação alérgica grave", "Paciente deu entrada com edema de face após uso de Sulfametoxazol",
                "T88", "Prednisona 5mg/dia, Epinefrina se necessário",
                "ALERTA: múltiplas alergias medicamentosas graves");

        salvarAtendimento(prontuarioId, drAndreId, clinicaId, LocalDate.of(2024, 5, 20),
                "Dermatite de contato", "Lesões eritematosas em mãos e antebraços",
                "L25", "Loratadina 10mg/dia, creme de hidrocortisona",
                "Evitar contato com substâncias irritantes");

        // Exames
        salvarExame(prontuarioId, "IgE total", LocalDate.of(2024, 1, 15),
                "IgE total: 850 UI/mL (muito elevado — referência: até 100 UI/mL)",
                hospitalId, drCarlosId);

        salvarExame(prontuarioId, "Hemograma completo", LocalDate.of(2024, 1, 15),
                "Eosinófilos: 12% (elevado), demais parâmetros normais",
                hospitalId, drCarlosId);

        return paciente;
    }

    // ==================== CAMILA FERREIRA — Respiratória + Endometriose (d) ====================

    private PacienteEntity criarCamilaFerreira(UUID hospitalId, UUID ubsId,
                                                 UUID drCarlosId, UUID draPatriciaId, UUID drRobertoId) {
        var paciente = salvarPaciente("Camila Ferreira", CPFS_VALIDOS[10], CNS_VALIDOS[10],
                LocalDate.of(1985, 3, 14), "F", "(11) 97777-3456");
        UUID pacienteId = paciente.getId();
        var prontuario = salvarProntuario(pacienteId);
        UUID prontuarioId = prontuario.getId();

        // Alergia: AAS (Ácido Acetilsalicílico)
        salvarAlergia(pacienteId, "Ácido Acetilsalicílico (AAS)", GravidadeAlergia.CRITICA,
                "Broncoespasmo severo e edema de glote — Tríade de Samter (asma + polipose nasal + intolerância a AAS)",
                drCarlosId);
        salvarAlergia(pacienteId, "Ibuprofeno", GravidadeAlergia.MODERADA,
                "Crise asmática após uso — reatividade cruzada com AAS",
                drRobertoId);

        // Medicamentos ativos
        salvarMedicamento(pacienteId, "Budesonida + Formoterol", "200/6mcg", "2x ao dia (inalatório)",
                LocalDate.of(2022, 3, 1), drCarlosId);
        salvarMedicamento(pacienteId, "Montelucaste", "10mg", "1x ao dia à noite",
                LocalDate.of(2022, 3, 1), drCarlosId);
        salvarMedicamento(pacienteId, "Furoato de mometasona nasal", "50mcg", "2 jatos em cada narina 1x/dia",
                LocalDate.of(2023, 5, 10), drRobertoId);
        salvarMedicamento(pacienteId, "Dienogeste", "2mg", "1x ao dia (contínuo)",
                LocalDate.of(2025, 11, 20), draPatriciaId);
        salvarMedicamento(pacienteId, "Paracetamol", "750mg", "Se dor (máx 4x/dia) — ÚNICO analgésico seguro",
                LocalDate.of(2022, 3, 1), drCarlosId);

        // Atendimentos — histórico respiratório crônico + endometriose
        salvarAtendimento(prontuarioId, drCarlosId, hospitalId, LocalDate.of(2022, 3, 1),
                "Crise asmática severa",
                "Paciente deu entrada com dispneia intensa, sibilância difusa, SatO2 89%. Relata uso de AAS para cefaleia há 2h. Histórico de asma desde infância.",
                "J45", "Budesonida+Formoterol 200/6mcg 2x/dia, Montelucaste 10mg/noite, Salbutamol SOS",
                "ALERTA: Tríade de Samter confirmada. PROIBIDO AAS, AINEs e derivados. Único analgésico seguro: Paracetamol.");

        salvarAtendimento(prontuarioId, drRobertoId, ubsId, LocalDate.of(2022, 8, 15),
                "Bronquite aguda com exacerbação de asma",
                "Tosse produtiva há 10 dias, febre baixa, sibilância. Espirometria: VEF1 65% do previsto.",
                "J20", "Azitromicina 500mg 1x/dia por 3 dias, manter Budesonida+Formoterol",
                "Reincidência de quadro respiratório. Solicitar TC de tórax se persistir.");

        salvarAtendimento(prontuarioId, drRobertoId, ubsId, LocalDate.of(2023, 5, 10),
                "Rinite alérgica persistente e sinusite",
                "Obstrução nasal bilateral crônica, rinorreia posterior, cefaleia frontal. Rinoscopia: cornetos hipertrofiados, secreção mucopurulenta em meato médio.",
                "J31", "Furoato de mometasona nasal 50mcg 2 jatos/narina/dia, lavagem nasal com SF",
                "Rinite alérgica persistente moderada-grave + sinusite crônica. Encaminhar para otorrino se não melhorar em 4 semanas.");

        salvarAtendimento(prontuarioId, drCarlosId, hospitalId, LocalDate.of(2023, 10, 20),
                "Sinusite aguda bacteriana",
                "Febre 38.5°C, dor facial intensa, secreção nasal purulenta há 10 dias. TC de seios da face: velamento de seio maxilar bilateral.",
                "J01", "Amoxicilina+Clavulanato 875/125mg 12/12h por 14 dias",
                "REINCIDÊNCIA de sinusite. Considerar avaliação cirúrgica (septoplastia + turbinectomia). Manter corticoide nasal.");

        salvarAtendimento(prontuarioId, draPatriciaId, ubsId, LocalDate.of(2024, 6, 5),
                "Dor pélvica crônica e dismenorreia severa",
                "Paciente refere dor pélvica crônica há 2 anos, dismenorreia incapacitante, dispareunia profunda, dor ao evacuar no período menstrual. Suspeita de endometriose profunda.",
                "N80", "Dienogeste 2mg/dia contínuo, Paracetamol 750mg se dor",
                "Solicitar RM de pelve com preparo intestinal. Encaminhar para centro de referência em endometriose.");

        salvarAtendimento(prontuarioId, draPatriciaId, hospitalId, LocalDate.of(2025, 9, 15),
                "Pré-operatório — Videolaparoscopia para endometriose profunda",
                "RM confirmou endometriose profunda com implantes em ligamento uterossacro bilateral, septo retovaginal e superfície vesical. Indicação cirúrgica.",
                "N80", "Preparo intestinal pré-operatório, suspender Dienogeste 24h antes",
                "Cirurgia agendada para 25/09/2025. Equipe multidisciplinar: ginecologista + cirurgião colorretal + urologista. ATENÇÃO: alergia a AAS — analgesia pós-op exclusivamente com Paracetamol e opioides.");

        salvarAtendimento(prontuarioId, drCarlosId, hospitalId, LocalDate.of(2025, 9, 25),
                "Pós-operatório — Videolaparoscopia endometriose profunda",
                "Cirurgia realizada com sucesso. Ressecção de implantes em ligamento uterossacro bilateral, nodulectomia de septo retovaginal e shaving vesical. Sem intercorrências. Alta hospitalar em 48h.",
                "N80", "Paracetamol 1g 6/6h, Tramadol 50mg 8/8h se dor intensa, Dienogeste 2mg/dia retomar em 7 dias",
                "Pós-op imediato sem complicações. PROIBIDO AINEs. Retorno ginecológico em 15 dias, retorno colorretal em 30 dias.");

        salvarAtendimento(prontuarioId, drRobertoId, ubsId, LocalDate.of(2026, 1, 8),
                "Exacerbação de asma pós-IVAS",
                "Quadro gripal há 5 dias evoluindo com piora da dispneia e sibilância. SatO2 93%. Pico de fluxo: 60% do melhor pessoal.",
                "J45", "Prednisona 40mg/dia por 5 dias, manter Budesonida+Formoterol, Salbutamol 4/4h",
                "Quinta exacerbação em 4 anos. Asma de difícil controle — considerar biológico (Omalizumabe). LEMBRAR: alergia a AAS/AINEs.");

        // Exames
        salvarExame(prontuarioId, "Espirometria", LocalDate.of(2022, 3, 15),
                "VEF1: 65% do previsto, CVF: 78%, VEF1/CVF: 0.68. Prova broncodilatadora positiva (melhora de 18%). Padrão obstrutivo moderado.",
                hospitalId, drCarlosId);

        salvarExame(prontuarioId, "IgE total", LocalDate.of(2022, 3, 15),
                "IgE total: 520 UI/mL (elevado — referência: até 100 UI/mL). Perfil atópico.",
                hospitalId, drCarlosId);

        salvarExame(prontuarioId, "TC de seios da face", LocalDate.of(2023, 10, 20),
                "Velamento de seio maxilar bilateral, espessamento mucoso de seios etmoidais. Desvio septal para direita. Hipertrofia de cornetos inferiores.",
                hospitalId, drCarlosId);

        salvarExame(prontuarioId, "Hemograma completo", LocalDate.of(2024, 6, 5),
                "Hemoglobina: 11.8g/dL (levemente baixa), Eosinófilos: 8% (elevado), demais parâmetros normais.",
                ubsId, draPatriciaId);

        salvarExame(prontuarioId, "CA-125", LocalDate.of(2024, 6, 5),
                "CA-125: 85 U/mL (elevado — referência: até 35 U/mL). Compatível com endometriose ativa.",
                ubsId, draPatriciaId);

        salvarExame(prontuarioId, "RM de pelve com preparo intestinal", LocalDate.of(2024, 7, 10),
                "Endometriose profunda: implantes em ligamento uterossacro bilateral (1.8cm à esquerda, 1.2cm à direita), nódulo em septo retovaginal (2.1cm), implante superficial em cúpula vesical (0.8cm). Adenomiose focal em parede posterior do útero.",
                hospitalId, draPatriciaId);

        salvarExame(prontuarioId, "Espirometria", LocalDate.of(2026, 1, 10),
                "VEF1: 58% do previsto (piora em relação a 2022), CVF: 75%, VEF1/CVF: 0.65. Prova broncodilatadora positiva. Asma persistente moderada-grave.",
                ubsId, drRobertoId);

        return paciente;
    }

    // ==================== PACIENTE SIMPLES (additional 7) ====================

    private PacienteEntity criarPacienteSimples(String nome, String cpf, String cns,
                                                 LocalDate dataNascimento, String sexo,
                                                 UUID unidadeId, UUID profissionalId,
                                                 String queixa, String cid,
                                                 String medicamento, String dosagem) {
        var paciente = salvarPaciente(nome, cpf, cns, dataNascimento, sexo, "(11) 9" + cpf.substring(0, 4) + "-" + cpf.substring(4, 8));
        UUID pacienteId = paciente.getId();
        var prontuario = salvarProntuario(pacienteId);

        salvarMedicamento(pacienteId, medicamento, dosagem, "1x ao dia",
                LocalDate.of(2024, 1, 1), profissionalId);

        salvarAtendimento(prontuario.getId(), profissionalId, unidadeId,
                LocalDate.of(2024, 3, 15), queixa,
                "Paciente refere " + queixa.toLowerCase() + " há alguns dias",
                cid, medicamento + " " + dosagem, "Retorno em 30 dias");

        salvarExame(prontuario.getId(), "Hemograma completo", LocalDate.of(2024, 3, 15),
                "Parâmetros dentro da normalidade", unidadeId, profissionalId);

        return paciente;
    }

    // ==================== USUÁRIOS ====================

    private void criarUsuarios(List<ProfissionalSaudeEntity> profissionais,
                                List<PacienteEntity> pacientes) {
        String senhaHash = passwordEncoder.encode("senha123");

        // medico1 — linked to Dr. Carlos Eduardo Mendes
        salvarUsuario("medico1", senhaHash, Role.PROFISSIONAL, profissionais.get(0).getId());

        // paciente1 — linked to Maria Silva
        salvarUsuario("paciente1", senhaHash, Role.PACIENTE, pacientes.get(0).getId());

        // gestor1
        salvarUsuario("gestor1", senhaHash, Role.GESTOR, null);
    }

    // ==================== HELPER METHODS ====================

    private PacienteEntity salvarPaciente(String nome, String cpf, String cns,
                                           LocalDate dataNascimento, String sexo, String contato) {
        Instant agora = Instant.now();
        var paciente = new Paciente(UUID.randomUUID(), nome, new CPF(cpf),
                new CNS(cns), dataNascimento, sexo, contato, agora, agora);
        return pacienteRepo.save(PacienteEntity.fromDomain(paciente));
    }

    private ProntuarioEntity salvarProntuario(UUID pacienteId) {
        var prontuario = Prontuario.criar(pacienteId);
        return prontuarioRepo.save(ProntuarioEntity.fromDomain(prontuario));
    }

    private void salvarAlergia(UUID pacienteId, String substancia, GravidadeAlergia gravidade,
                                String reacao, UUID profissionalId) {
        var alergia = Alergia.criar(pacienteId, substancia, gravidade, reacao, profissionalId);
        alergiaRepo.save(AlergiaEntity.fromDomain(alergia));
    }

    private void salvarMedicamento(UUID pacienteId, String nome, String dosagem, String frequencia,
                                    LocalDate dataInicio, UUID profissionalId) {
        var med = MedicamentoEmUso.criar(pacienteId, nome, dosagem, frequencia, dataInicio, profissionalId);
        medicamentoRepo.save(MedicamentoEmUsoEntity.fromDomain(med));
    }

    private void salvarAtendimento(UUID prontuarioId, UUID profissionalId, UUID unidadeId,
                                    LocalDate data, String queixa, String anamnese,
                                    String cid, String prescricoes, String observacoes) {
        var atendimento = RegistroAtendimento.criar(prontuarioId, profissionalId, unidadeId,
                data, queixa, anamnese, cid, prescricoes, observacoes);
        atendimentoRepo.save(RegistroAtendimentoEntity.fromDomain(atendimento));
    }

    private void salvarExame(UUID prontuarioId, String tipo, LocalDate data, String resultado,
                              UUID unidadeId, UUID profissionalId) {
        var exame = Exame.criar(prontuarioId, tipo, data, resultado, unidadeId, profissionalId);
        exameRepo.save(ExameEntity.fromDomain(exame));
    }

    private void salvarUsuario(String username, String senhaHash, Role role, UUID referenciaId) {
        var usuario = new Usuario(UUID.randomUUID(), username, senhaHash, role, referenciaId, true);
        usuarioRepo.save(UsuarioEntity.fromDomain(usuario));
    }
}
