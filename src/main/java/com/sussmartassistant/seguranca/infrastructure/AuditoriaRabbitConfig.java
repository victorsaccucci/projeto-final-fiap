package com.sussmartassistant.seguranca.infrastructure;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Configuração das filas RabbitMQ para auditoria.
 */
@Configuration
@Profile("prod")
public class AuditoriaRabbitConfig {

    public static final String QUEUE = "auditoria.eventos";
    public static final String DLQ = "auditoria.eventos.dlq";
    public static final String EXCHANGE = "auditoria.exchange";
    public static final String ROUTING_KEY = "auditoria.eventos";

    @Bean
    public Queue auditoriaDlq() {
        return QueueBuilder.durable(DLQ).build();
    }

    @Bean
    public Queue auditoriaQueue() {
        return QueueBuilder.durable(QUEUE)
                .withArgument("x-dead-letter-exchange", "")
                .withArgument("x-dead-letter-routing-key", DLQ)
                .build();
    }

    @Bean
    public DirectExchange auditoriaExchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Binding auditoriaBinding(Queue auditoriaQueue, DirectExchange auditoriaExchange) {
        return BindingBuilder.bind(auditoriaQueue).to(auditoriaExchange).with(ROUTING_KEY);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
