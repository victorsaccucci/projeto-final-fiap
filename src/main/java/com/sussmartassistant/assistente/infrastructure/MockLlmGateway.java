package com.sussmartassistant.assistente.infrastructure;

import com.sussmartassistant.assistente.application.LlmGateway;
import com.sussmartassistant.assistente.application.LlmRequest;
import com.sussmartassistant.assistente.application.LlmResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Gateway mock para LLM em ambiente dev (quando Ollama não está disponível).
 * Retorna hipóteses diagnósticas realistas baseadas em sintomas comuns.
 */
public class MockLlmGateway implements LlmGateway {

    private static final Logger log = LoggerFactory.getLogger(MockLlmGateway.class);

    private final LlmGateway ollamaGateway;
    private final Random random = new Random();

    /**
     * @param ollamaGateway gateway real do Ollama — tenta primeiro, usa mock como fallback
     */
    public MockLlmGateway(LlmGateway ollamaGateway) {
        this.ollamaGateway = ollamaGateway;
    }

    @Override
    public LlmResponse enviar(LlmRequest request) {
        // Tenta Ollama primeiro
        try {
            LlmResponse response = ollamaGateway.enviar(request);
            log.info("Ollama respondeu com sucesso — usando resposta real");
            return response;
        } catch (Exception e) {
            log.warn("Ollama indisponível ({}), usando respostas mock para demo", e.getMessage());
        }

        // Fallback: gerar resposta mock baseada nos sintomas do prompt
        String prompt = request.prompt().toLowerCase();
        String mockResponse = gerarRespostaMock(prompt);
        log.info("Resposta mock gerada ({} caracteres)", mockResponse.length());
        return new LlmResponse(mockResponse);
    }

    private String gerarRespostaMock(String prompt) {
        // Detectar sintomas no prompt e retornar hipóteses realistas
        if (prompt.contains("dor de cabeça") || prompt.contains("cefaleia") || prompt.contains("cefaléia")) {
            return HIPOTESES_CEFALEIA;
        }
        if (prompt.contains("dor torácica") || prompt.contains("dor no peito") || prompt.contains("precordial")) {
            return HIPOTESES_DOR_TORACICA;
        }
        if (prompt.contains("febre") && (prompt.contains("tosse") || prompt.contains("garganta"))) {
            return HIPOTESES_IVAS;
        }
        if (prompt.contains("febre")) {
            return HIPOTESES_FEBRE;
        }
        if (prompt.contains("dor abdominal") || prompt.contains("dor epigástrica") || prompt.contains("barriga")) {
            return HIPOTESES_DOR_ABDOMINAL;
        }
        if (prompt.contains("falta de ar") || prompt.contains("dispneia") || prompt.contains("dispnéia")) {
            return HIPOTESES_DISPNEIA;
        }
        if (prompt.contains("tontura") || prompt.contains("vertigem")) {
            return HIPOTESES_TONTURA;
        }
        if (prompt.contains("pele") || prompt.contains("erupção") || prompt.contains("coceira") || prompt.contains("prurido")) {
            return HIPOTESES_DERMATOLOGICA;
        }
        // Resposta genérica
        return HIPOTESES_GENERICA;
    }

    private static final String HIPOTESES_CEFALEIA = """
        [
          {
            "codigoCid": "G43",
            "justificativa": "Enxaqueca — cefaleia recorrente, frequentemente unilateral, com possível associação a náusea e fotofobia. Padrão compatível com histórico do paciente.",
            "confianca": "ALTA"
          },
          {
            "codigoCid": "G44.2",
            "justificativa": "Cefaleia tensional — dor em pressão bilateral, frequentemente associada a estresse e tensão muscular cervical. Diagnóstico diferencial mais comum.",
            "confianca": "MEDIA"
          },
          {
            "codigoCid": "I10",
            "justificativa": "Hipertensão arterial — cefaleia pode ser manifestação de pico hipertensivo. Verificar níveis pressóricos e histórico cardiovascular.",
            "confianca": "BAIXA"
          }
        ]""";

    private static final String HIPOTESES_DOR_TORACICA = """
        [
          {
            "codigoCid": "I20",
            "justificativa": "Angina pectoris — dor torácica que pode indicar isquemia miocárdica. Necessário ECG e enzimas cardíacas para exclusão de síndrome coronariana aguda.",
            "confianca": "ALTA"
          },
          {
            "codigoCid": "R07.4",
            "justificativa": "Dor torácica inespecífica — pode ter origem musculoesquelética ou ansiedade. Importante excluir causas cardíacas e pulmonares.",
            "confianca": "MEDIA"
          },
          {
            "codigoCid": "K21",
            "justificativa": "Doença do refluxo gastroesofágico — dor retroesternal que pode mimetizar dor cardíaca. Avaliar relação com alimentação e posição.",
            "confianca": "BAIXA"
          }
        ]""";

    private static final String HIPOTESES_IVAS = """
        [
          {
            "codigoCid": "J06",
            "justificativa": "Infecção das vias aéreas superiores — quadro clássico com febre, odinofagia e sintomas respiratórios altos. Etiologia viral mais provável.",
            "confianca": "ALTA"
          },
          {
            "codigoCid": "J03",
            "justificativa": "Amigdalite aguda — febre associada a dor de garganta intensa sugere possível infecção bacteriana. Avaliar necessidade de teste rápido para Streptococcus.",
            "confianca": "MEDIA"
          },
          {
            "codigoCid": "J01",
            "justificativa": "Sinusite aguda — congestão nasal com febre pode indicar sinusite bacteriana secundária. Avaliar duração dos sintomas e presença de secreção purulenta.",
            "confianca": "BAIXA"
          }
        ]""";

    private static final String HIPOTESES_FEBRE = """
        [
          {
            "codigoCid": "A09",
            "justificativa": "Gastroenterite infecciosa — febre com possível comprometimento gastrointestinal. Causa comum de febre aguda em adultos.",
            "confianca": "MEDIA"
          },
          {
            "codigoCid": "N39",
            "justificativa": "Infecção do trato urinário — febre pode indicar infecção urinária, especialmente em mulheres. Solicitar urina tipo I e urocultura.",
            "confianca": "MEDIA"
          },
          {
            "codigoCid": "B34",
            "justificativa": "Infecção viral não especificada — síndrome febril aguda de provável etiologia viral. Manter observação e tratamento sintomático.",
            "confianca": "BAIXA"
          }
        ]""";

    private static final String HIPOTESES_DOR_ABDOMINAL = """
        [
          {
            "codigoCid": "K29",
            "justificativa": "Gastrite — dor epigástrica com possível relação alimentar. Avaliar uso de AINEs e histórico de H. pylori.",
            "confianca": "ALTA"
          },
          {
            "codigoCid": "K80",
            "justificativa": "Colelitíase — dor abdominal em hipocôndrio direito, especialmente pós-prandial. Solicitar ultrassonografia abdominal.",
            "confianca": "MEDIA"
          },
          {
            "codigoCid": "K59",
            "justificativa": "Distúrbio funcional do intestino — dor abdominal difusa sem sinais de alarme. Considerar síndrome do intestino irritável.",
            "confianca": "BAIXA"
          }
        ]""";

    private static final String HIPOTESES_DISPNEIA = """
        [
          {
            "codigoCid": "J45",
            "justificativa": "Asma brônquica — dispneia com possível sibilância. Avaliar histórico de atopia e resposta a broncodilatadores.",
            "confianca": "ALTA"
          },
          {
            "codigoCid": "I50",
            "justificativa": "Insuficiência cardíaca — dispneia pode indicar congestão pulmonar. Avaliar edema de membros inferiores e ortopneia.",
            "confianca": "MEDIA"
          },
          {
            "codigoCid": "F41",
            "justificativa": "Transtorno de ansiedade — dispneia funcional associada a ansiedade. Diagnóstico de exclusão após afastar causas orgânicas.",
            "confianca": "BAIXA"
          }
        ]""";

    private static final String HIPOTESES_TONTURA = """
        [
          {
            "codigoCid": "H81",
            "justificativa": "Vertigem posicional paroxística benigna — tontura rotatória associada a mudanças de posição. Realizar manobra de Dix-Hallpike.",
            "confianca": "ALTA"
          },
          {
            "codigoCid": "I95",
            "justificativa": "Hipotensão — tontura ao levantar pode indicar hipotensão ortostática. Verificar medicamentos em uso e hidratação.",
            "confianca": "MEDIA"
          },
          {
            "codigoCid": "D50",
            "justificativa": "Anemia ferropriva — tontura crônica pode estar associada a anemia. Solicitar hemograma completo e ferritina.",
            "confianca": "BAIXA"
          }
        ]""";

    private static final String HIPOTESES_DERMATOLOGICA = """
        [
          {
            "codigoCid": "L20",
            "justificativa": "Dermatite atópica — lesões pruriginosas recorrentes em áreas típicas. Avaliar histórico de atopia familiar.",
            "confianca": "ALTA"
          },
          {
            "codigoCid": "L50",
            "justificativa": "Urticária — erupção cutânea com prurido intenso. Investigar possíveis alérgenos e medicamentos em uso.",
            "confianca": "MEDIA"
          },
          {
            "codigoCid": "B35",
            "justificativa": "Dermatofitose — infecção fúngica cutânea. Avaliar características das lesões e solicitar exame micológico direto.",
            "confianca": "BAIXA"
          }
        ]""";

    private static final String HIPOTESES_GENERICA = """
        [
          {
            "codigoCid": "R69",
            "justificativa": "Causa desconhecida de morbidade — sintomas inespecíficos que requerem investigação complementar. Solicitar exames laboratoriais básicos.",
            "confianca": "MEDIA"
          },
          {
            "codigoCid": "Z00",
            "justificativa": "Exame geral e investigação — quadro clínico que necessita avaliação mais detalhada com exames complementares para definição diagnóstica.",
            "confianca": "MEDIA"
          },
          {
            "codigoCid": "R53",
            "justificativa": "Mal-estar e fadiga — sintomas gerais que podem estar associados a diversas condições. Avaliar estado nutricional e emocional do paciente.",
            "confianca": "BAIXA"
          }
        ]""";
}
