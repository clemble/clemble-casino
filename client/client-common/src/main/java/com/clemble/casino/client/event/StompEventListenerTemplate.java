package com.clemble.casino.client.event;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import javax.security.auth.login.LoginException;

import net.ser1.stomp.Client;
import net.ser1.stomp.Listener;

import com.clemble.casino.configuration.NotificationConfiguration;
import com.clemble.casino.configuration.NotificationHost;
import com.clemble.casino.event.Event;
import com.fasterxml.jackson.databind.ObjectMapper;

public class StompEventListenerTemplate extends AbstractEventListenerTemplate {

    /**
     * Generated 29/11/13
     */
    private static final long serialVersionUID = -3708639830312251122L;

    final private StompListener listener;
    final private AtomicReference<Client> client = new AtomicReference<Client>();

    public StompEventListenerTemplate(String player, NotificationConfiguration configuration, ObjectMapper objectMapper) {
        super(player);
        this.listener = new StompListener(objectMapper);
        this.executor.schedule(new StompStartupRunnable(configuration, objectMapper), 0, TimeUnit.SECONDS);
    }

    final public class StompStartupRunnable implements Runnable {

        final private NotificationConfiguration configuration;
        final private ObjectMapper objectMapper;

        public StompStartupRunnable(NotificationConfiguration configurations, ObjectMapper objectMapper) {
            this.configuration = configurations;
            this.objectMapper = objectMapper;
        }

        @Override
        public void run() {
            // Step 1. Creating a game table client
            try {
                final NotificationHost stompNotification = configuration.getStompHost();
                final Client stompClient = new Client(stompNotification.getHost(), stompNotification.getPort(), configuration.getUser(),
                        configuration.getPassword());
                // Step 2. Returning game listener with possibility to stop STOMP listener
                connectionCleaner.set(new Closeable() {
                    @Override
                    public void close() throws IOException {
                        stompClient.disconnect();
                    }
                });
                client.set(stompClient);
                refreshSubscription();
            } catch (LoginException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                executor.schedule(new StompStartupRunnable(configuration, objectMapper), 30, TimeUnit.SECONDS);
            }
        }
    }

    public class StompListener implements Listener {

        final private ObjectMapper objectMapper;

        public StompListener(ObjectMapper objectMapper) {
            this.objectMapper = checkNotNull(objectMapper);
        }

        @Override
        public void message(@SuppressWarnings("rawtypes") Map parameters, String message) {
            try {
                Event event = objectMapper.readValue(message, Event.class);
                // Step 1. Reading and notifying of the message
                update(String.valueOf(parameters.get("destination")).substring(7), event);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void subscribe(String channel) {
        if (client.get() != null)
            client.get().subscribe("/topic/" + channel, listener);
    }

    @Override
    public boolean isAlive() {
        return client.get() != null;
    }

}