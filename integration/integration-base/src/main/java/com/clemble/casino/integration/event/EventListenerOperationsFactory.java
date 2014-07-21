package com.clemble.casino.integration.event;

import com.clemble.casino.client.event.EventListenerOperations;
import com.clemble.casino.client.event.RabbitEventListenerTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;

abstract public class EventListenerOperationsFactory {

    abstract public EventListenerOperations construct(String player, String host, ObjectMapper objectMapper);

    public static class RabbitEventListenerServiceFactory extends EventListenerOperationsFactory {

        @Override
        public EventListenerOperations construct(String player, String host, ObjectMapper objectMapper) {
            return new RabbitEventListenerTemplate(player, host, objectMapper);
        }

    }

}
