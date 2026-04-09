package com.sussmartassistant.assistente.infrastructure;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Configuração das filas RabbitMQ para o módulo Assistente Inteligente.
 * Ativa apenas fora do profile dev (em prod, quando RabbitMQ está disponível).
 */
@Configuration
@Profile("prod")
public class AssistenteRabbitConfig {

    public static final String QUEUE = "assistente.solicitacoes";
    public static final String DLQ = "assistente.solicitacoes.dlq";
    public static final String EXCHANGE = "assistente.exchange";
    public static final String ROUTING_KEY = "assistente.solicitacoes";

    @Bean
    public Queue assistenteDlq() {
        return QueueBuilder.durable(DLQ).build();
    }

    @Bean
    public Queue assistenteQueue() {
        return QueueBuilder.durable(QUEUE)
                .withArgument("x-dead-letter-exchange", "")
                .withArgument("x-dead-letter-routing-key", DLQ)
                .build();
    }

    @Bean
    public DirectExchange assistenteExchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Binding assistenteBinding(@Qualifier("assistenteQueue") Queue assistenteQueue,
                                      @Qualifier("assistenteExchange") DirectExchange assistenteExchange) {
        return BindingBuilder.bind(assistenteQueue).to(assistenteExchange).with(ROUTING_KEY);
    }
}
