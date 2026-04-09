package com.sussmartassistant.metricas.infrastructure;

import com.sussmartassistant.metricas.application.ConsultarDiagnosticosFrequentesUseCase;
import com.sussmartassistant.metricas.application.ConsultarMetricasAssistenteUseCase;
import com.sussmartassistant.metricas.application.ConsultarMetricasAtendimentoUseCase;
import com.sussmartassistant.metricas.application.MetricasRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricasModuleConfig {

    @Bean
    public ConsultarMetricasAtendimentoUseCase consultarMetricasAtendimentoUseCase(
            MetricasRepository metricasRepository) {
        return new ConsultarMetricasAtendimentoUseCase(metricasRepository);
    }

    @Bean
    public ConsultarDiagnosticosFrequentesUseCase consultarDiagnosticosFrequentesUseCase(
            MetricasRepository metricasRepository) {
        return new ConsultarDiagnosticosFrequentesUseCase(metricasRepository);
    }

    @Bean
    public ConsultarMetricasAssistenteUseCase consultarMetricasAssistenteUseCase(
            MetricasRepository metricasRepository) {
        return new ConsultarMetricasAssistenteUseCase(metricasRepository);
    }
}
