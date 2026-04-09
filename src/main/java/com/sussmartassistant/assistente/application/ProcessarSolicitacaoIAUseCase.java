package com.sussmartassistant.assistente.application;

import com.sussmartassistant.assistente.domain.HipoteseDiagnostica;
import com.sussmartassistant.assistente.domain.SolicitacaoIA;
import com.sussmartassistant.paciente.domain.Alergia;
import com.sussmartassistant.prontuario.application.ConsultarProntuarioCompletoUseCase;
import com.sussmartassistant.prontuario.application.ProntuarioCompleto;
import com.sussmartassistant.prontuario.domain.RegistroAtendimento;
import com.sussmartassistant.shared.domain.LlmUnavailableException;
import com.sussmartassistant.shared.domain.NivelConfianca;
import com.sussmartassistant.shared.domain.ResourceNotFoundException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Use case para processar solicitação de IA (consumer RabbitMQ).
 * Busca prontuário completo, monta prompt, envia ao LLM, parseia resposta,
 * detecta reincidência e salva resultado.
 */
public class ProcessarSolicitacaoIAUseCase {

    private static final Logger log = LoggerFactory.getLogger(ProcessarSolicitacaoIAUseCase.class);

    private final SolicitacaoIARepository solicitacaoRepository;
    private final ConsultarProntuarioCompletoUseCase consultarProntuario;
    private final PromptBuilder promptBuilder;
    private final LlmGateway llmGateway;
    private final String modelName;
    private final ObjectMapper objectMapper;

    public ProcessarSolicitacaoIAUseCase(SolicitacaoIARepository solicitacaoRepository,
                                          ConsultarProntuarioCompletoUseCase consultarProntuario,
                                          PromptBuilder promptBuilder,
                                          LlmGateway llmGateway,
                                          String modelName) {
        this.solicitacaoRepository = solicitacaoRepository;
        this.consultarProntuario = consultarProntuario;
        this.promptBuilder = promptBuilder;
        this.llmGateway = llmGateway;
        this.modelName = modelName;
        this.objectMapper = new ObjectMapper();
    }

    public void executar(UUID solicitacaoId) {
        SolicitacaoIA solicitacao = solicitacaoRepository.buscarPorId(solicitacaoId)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitação IA não encontrada: " + solicitacaoId));

        solicitacao.iniciarProcessamento();
        solicitacaoRepository.salvar(solicitacao);

        try {
            ProntuarioCompleto prontuario = consultarProntuario.executar(solicitacao.getPacienteId());

            List<String> sintomas = Arrays.asList(solicitacao.getSintomasInformados().split(";\\s*"));
            String prompt = promptBuilder.construirPrompt(prontuario, sintomas);

            LlmRequest request = new LlmRequest(modelName, prompt);
            LlmResponse response = llmGateway.enviar(request);

            List<HipoteseDiagnostica> hipoteses = parsearResposta(solicitacao.getId(), response.response());

            // Detect reincidence
            Set<String> cidsAnteriores = extrairCidsAnteriores(prontuario);
            for (HipoteseDiagnostica h : hipoteses) {
                if (cidsAnteriores.contains(h.getCodigoCid())) {
                    h.setReincidencia(true);
                }
            }

            // Detect allergy contraindications
            detectarContraindicacoes(hipoteses, prontuario.alergias());

            solicitacao.concluir(prompt, response.response(), hipoteses);
            solicitacaoRepository.salvar(solicitacao);

            log.info("Solicitação IA {} processada com sucesso. {} hipóteses geradas.",
                    solicitacaoId, hipoteses.size());

        } catch (LlmUnavailableException e) {
            log.error("LLM indisponível ao processar solicitação {}: {}", solicitacaoId, e.getMessage());
            solicitacao.marcarErro("Serviço de IA temporariamente indisponível: " + e.getMessage());
            solicitacaoRepository.salvar(solicitacao);
        } catch (Exception e) {
            log.error("Erro ao processar solicitação IA {}: {}", solicitacaoId, e.getMessage(), e);
            solicitacao.marcarErro("Erro no processamento: " + e.getMessage());
            solicitacaoRepository.salvar(solicitacao);
        }
    }

    List<HipoteseDiagnostica> parsearResposta(UUID solicitacaoId, String respostaLlm) {
        if (respostaLlm == null || respostaLlm.isBlank()) {
            return List.of();
        }

        try {
            // Try to extract JSON array from the response
            String jsonContent = extrairJson(respostaLlm);
            List<Map<String, Object>> items = objectMapper.readValue(jsonContent, new TypeReference<>() {});

            List<HipoteseDiagnostica> hipoteses = new ArrayList<>();
            for (int i = 0; i < items.size(); i++) {
                Map<String, Object> item = items.get(i);
                String cid = String.valueOf(item.getOrDefault("codigoCid", ""));
                String justificativa = String.valueOf(item.getOrDefault("justificativa", ""));
                String confiancaStr = String.valueOf(item.getOrDefault("confianca", "MEDIA"));

                NivelConfianca confianca;
                try {
                    confianca = NivelConfianca.valueOf(confiancaStr.toUpperCase());
                } catch (IllegalArgumentException e) {
                    confianca = NivelConfianca.MEDIA;
                }

                if (!cid.isBlank() && !justificativa.isBlank()) {
                    hipoteses.add(HipoteseDiagnostica.criar(solicitacaoId, cid, justificativa,
                            confianca, false, null, i + 1));
                }
            }
            return hipoteses;

        } catch (Exception e) {
            log.warn("Falha ao parsear resposta JSON do LLM, criando hipótese única com texto bruto: {}", e.getMessage());
            return List.of(HipoteseDiagnostica.criar(solicitacaoId, "N/A", respostaLlm,
                    NivelConfianca.BAIXA, false, null, 1));
        }
    }

    private String extrairJson(String texto) {
        // Find JSON array in the response text
        int start = texto.indexOf('[');
        int end = texto.lastIndexOf(']');
        if (start >= 0 && end > start) {
            return texto.substring(start, end + 1);
        }
        return texto;
    }

    private Set<String> extrairCidsAnteriores(ProntuarioCompleto prontuario) {
        Set<String> cids = new HashSet<>();
        for (RegistroAtendimento atendimento : prontuario.atendimentos()) {
            if (atendimento.getDiagnosticoCid() != null && !atendimento.getDiagnosticoCid().isBlank()) {
                cids.add(atendimento.getDiagnosticoCid());
            }
        }
        return cids;
    }

    private void detectarContraindicacoes(List<HipoteseDiagnostica> hipoteses, List<Alergia> alergias) {
        if (alergias == null || alergias.isEmpty()) return;

        StringBuilder alertaBase = new StringBuilder("ATENÇÃO - Alergias do paciente: ");
        for (int i = 0; i < alergias.size(); i++) {
            Alergia a = alergias.get(i);
            if (i > 0) alertaBase.append(", ");
            alertaBase.append(a.getSubstancia()).append(" (").append(a.getGravidade()).append(")");
        }
        String alerta = alertaBase.toString();

        for (HipoteseDiagnostica h : hipoteses) {
            h.setAlertaContraindicacao(alerta);
        }
    }
}
