package com.clemble.casino.integration.event;

import com.clemble.casino.client.event.EventListenerOperation;
import com.clemble.casino.client.event.RabbitEventListenerTemplate;
import com.clemble.casino.client.event.StompEventListenerTemplate;
import com.clemble.casino.configuration.NotificationConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;

abstract public class EventListenerOperationsFactory {

    abstract public EventListenerOperation construct(NotificationConfiguration configuration, ObjectMapper objectMapper);

    public static class RabbitEventListenerServiceFactory extends EventListenerOperationsFactory {

        @Override
        public EventListenerOperation construct(NotificationConfiguration configuration, ObjectMapper objectMapper) {
            return new RabbitEventListenerTemplate(configuration, objectMapper);
        }

    }

    public static class StompEventListenerServiceFactory extends EventListenerOperationsFactory {

        @Override
        public EventListenerOperation construct(NotificationConfiguration configuration, ObjectMapper objectMapper) {
            return new StompEventListenerTemplate(configuration, objectMapper);
        }

    }

}
