package com.clemble.casino.integration.event;

import com.clemble.casino.client.event.EventListenerOperations;
import com.clemble.casino.client.event.RabbitEventListenerTemplate;
import com.clemble.casino.configuration.NotificationConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;

abstract public class EventListenerOperationsFactory {

    abstract public EventListenerOperations construct(String player, NotificationConfiguration configuration, ObjectMapper objectMapper);

    public static class RabbitEventListenerServiceFactory extends EventListenerOperationsFactory {

        @Override
        public EventListenerOperations construct(String player, NotificationConfiguration configuration, ObjectMapper objectMapper) {
            return new RabbitEventListenerTemplate(player, configuration, objectMapper);
        }

    }

}
