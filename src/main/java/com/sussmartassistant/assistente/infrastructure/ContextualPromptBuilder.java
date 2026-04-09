package com.sussmartassistant.assistente.infrastructure;

import com.sussmartassistant.assistente.application.PromptBuilder;
import com.sussmartassistant.paciente.domain.Alergia;
import com.sussmartassistant.paciente.domain.MedicamentoEmUso;
import com.sussmartassistant.prontuario.application.ProntuarioCompleto;
import com.sussmartassistant.prontuario.domain.Exame;
import com.sussmartassistant.prontuario.domain.RegistroAtendimento;

import java.util.List;

/**
 * Implementação do PromptBuilder que monta prompt contextualizado com dados completos do prontuário.
 */
public class ContextualPromptBuilder implements PromptBuilder {

    @Override
    public String construirPrompt(ProntuarioCompleto dados, List<String> sintomasAtuais) {
        StringBuilder sb = new StringBuilder();

        sb.append("Você é um assistente médico especializado em sugerir hipóteses diagnósticas.\n");
        sb.append("Analise os dados clínicos do paciente e os sintomas atuais informados.\n\n");

        // Alergias
        sb.append("=== ALERGIAS DO PACIENTE ===\n");
        if (dados.alergias() != null && !dados.alergias().isEmpty()) {
            for (Alergia a : dados.alergias()) {
                sb.append("- ").append(a.getSubstancia())
                  .append(" (Gravidade: ").append(a.getGravidade())
                  .append(", Reação: ").append(a.getReacaoObservada()).append(")\n");
            }
            sb.append("\n⚠️ IMPORTANTE: Considere as alergias acima ao sugerir hipóteses. ");
            sb.append("Inclua alertas de contraindicação se algum tratamento comum para o diagnóstico ");
            sb.append("puder interagir com as alergias listadas.\n\n");
        } else {
            sb.append("Nenhuma alergia registrada.\n\n");
        }

        // Medicamentos ativos
        sb.append("=== MEDICAMENTOS EM USO ===\n");
        if (dados.medicamentosAtivos() != null && !dados.medicamentosAtivos().isEmpty()) {
            for (MedicamentoEmUso m : dados.medicamentosAtivos()) {
                sb.append("- ").append(m.getNome())
                  .append(" ").append(m.getDosagem())
                  .append(" (").append(m.getFrequencia()).append(")\n");
            }
        } else {
            sb.append("Nenhum medicamento em uso.\n");
        }
        sb.append("\n");

        // Diagnósticos anteriores (CIDs)
        sb.append("=== DIAGNÓSTICOS ANTERIORES ===\n");
        if (dados.atendimentos() != null && !dados.atendimentos().isEmpty()) {
            for (RegistroAtendimento at : dados.atendimentos()) {
                if (at.getDiagnosticoCid() != null && !at.getDiagnosticoCid().isBlank()) {
                    sb.append("- CID: ").append(at.getDiagnosticoCid())
                      .append(" (Data: ").append(at.getData())
                      .append(", Queixa: ").append(at.getQueixaPrincipal()).append(")\n");
                }
            }
        } else {
            sb.append("Nenhum atendimento anterior registrado.\n");
        }
        sb.append("\n");

        // Exames relevantes
        sb.append("=== EXAMES RECENTES ===\n");
        if (dados.exames() != null && !dados.exames().isEmpty()) {
            for (Exame e : dados.exames()) {
                sb.append("- ").append(e.getTipo())
                  .append(" (").append(e.getDataRealizacao()).append("): ")
                  .append(e.getResultado()).append("\n");
            }
        } else {
            sb.append("Nenhum exame registrado.\n");
        }
        sb.append("\n");

        // Sintomas atuais
        sb.append("=== SINTOMAS ATUAIS ===\n");
        for (String sintoma : sintomasAtuais) {
            sb.append("- ").append(sintoma).append("\n");
        }
        sb.append("\n");

        // Instruções de resposta
        sb.append("=== INSTRUÇÕES ===\n");
        sb.append("Com base nos dados acima, sugira até 5 hipóteses diagnósticas.\n");
        sb.append("Responda EXCLUSIVAMENTE com um array JSON no seguinte formato:\n");
        sb.append("[\n");
        sb.append("  {\n");
        sb.append("    \"codigoCid\": \"código CID-10\",\n");
        sb.append("    \"justificativa\": \"explicação clínica da hipótese\",\n");
        sb.append("    \"confianca\": \"ALTA\" | \"MEDIA\" | \"BAIXA\"\n");
        sb.append("  }\n");
        sb.append("]\n");
        sb.append("Ordene as hipóteses da mais provável para a menos provável.\n");
        sb.append("Não inclua texto fora do JSON.\n");

        return sb.toString();
    }
}
