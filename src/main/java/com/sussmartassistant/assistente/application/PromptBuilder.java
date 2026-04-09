package com.sussmartassistant.assistente.application;

import com.sussmartassistant.prontuario.application.ProntuarioCompleto;

import java.util.List;

/**
 * Interface para construção de prompts contextualizados para o LLM.
 */
public interface PromptBuilder {
    String construirPrompt(ProntuarioCompleto dadosProntuario, List<String> sintomasAtuais);
}
