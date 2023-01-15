package org.cyrilselyanin.vendingsystem.regularbus.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Message queuing configuration
 */
@EnableRabbit
@Configuration
@EnableConfigurationProperties(ConfigProps.class)
public class MqConfig {
    /**
     * Direct exchange bean
     * @return instance of direct exchange
     */
    @Bean
    public DirectExchange direct(ConfigProps configProps) {
        return new DirectExchange(configProps.getMqExchange());
    }

    /**
     * Configuration for queue, binding and receive instance
     */
    private static class ReceiveConfig {
        /**
         * Queue bean, with a strict name (non-durable, exclusive, auto-delete).
         * Used with SPEL {autoDeletingQueue.name}
         * @return some queue
         */
        @Bean
        public Queue autoDeletingQueue(ConfigProps configProps) {
            // return new AnonymousQueue();
            return new Queue(configProps.getMqQueue(), false, false, true);
        }

        /**
         * Binding for regcash routing key
         * @param directExchange an exchange (direct)
         * @param queue auto deleting queue
         * @return a configured binding
         */
        @Bean
        public Binding binding(DirectExchange directExchange, Queue queue, ConfigProps configProps) {
            return BindingBuilder.
                    bind(queue)
                    .to(directExchange)
                    .with(configProps.getMqRoutingKey());
        }

        /**
         * Custom rabbitMQ template with JSON converting
         * @param connectionFactory some connection factory
         * @return custom template
         */
        @Bean
        public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
            final RabbitTemplate template = new RabbitTemplate(connectionFactory);
            template.setMessageConverter(producerJackson2JsonMessageConverter());
            return template;
        }

        /**
         * Json message converter producer
         * @return instance of the converter
         */
        @Bean
        public Jackson2JsonMessageConverter producerJackson2JsonMessageConverter() {
            return new Jackson2JsonMessageConverter();
        }
    }
}
