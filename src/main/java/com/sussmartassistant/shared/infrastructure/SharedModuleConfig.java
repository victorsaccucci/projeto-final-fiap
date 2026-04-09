package com.sussmartassistant.shared.infrastructure;

import com.sussmartassistant.shared.application.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SharedModuleConfig {

    @Bean
    public CadastrarUnidadeSaudeUseCase cadastrarUnidadeSaudeUseCase(UnidadeSaudeRepository unidadeSaudeRepository) {
        return new CadastrarUnidadeSaudeUseCase(unidadeSaudeRepository);
    }

    @Bean
    public ListarUnidadesSaudeUseCase listarUnidadesSaudeUseCase(UnidadeSaudeRepository unidadeSaudeRepository) {
        return new ListarUnidadesSaudeUseCase(unidadeSaudeRepository);
    }

    @Bean
    public CadastrarProfissionalUseCase cadastrarProfissionalUseCase(ProfissionalSaudeRepository profissionalSaudeRepository) {
        return new CadastrarProfissionalUseCase(profissionalSaudeRepository);
    }
}
