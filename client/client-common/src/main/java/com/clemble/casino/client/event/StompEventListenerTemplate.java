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

    final private AtomicReference<Client> client = new AtomicReference<Client>();
    final private StompListener listener;
    final private NotificationConfiguration notificationConfiguration;

    public StompEventListenerTemplate(String player, NotificationConfiguration configuration, ObjectMapper objectMapper) {
        super(player);
        this.notificationConfiguration = checkNotNull(configuration);
        this.listener = new StompListener(objectMapper);
        this.executor.schedule(new StompStartupRunnable(), 0, TimeUnit.SECONDS);
    }

    public class StompStartupRunnable implements Runnable {

        @Override
        public void run() {
            // Step 1. Creating a game table client
            try {
                final NotificationHost stompNotification = notificationConfiguration.getStompHost();
                final Client stompClient = new Client(stompNotification.getHost(), stompNotification.getPort(), notificationConfiguration.getUser(), notificationConfiguration.getPassword());
                // Step 4. Returning game listener with possibility to stop STOMP listener
                connectionCleaner.set(new Closeable() {

                    @Override
                    public void close() throws IOException {
                        stompClient.disconnect();
                    }

                });
                client.set(stompClient);
                subscribe(notificationConfiguration.getRoutingKey());
            } catch (LoginException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
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
                // Step 1. Reading and notifying of the message
                update(String.valueOf(parameters.get("routingKey")), objectMapper.readValue(message, Event.class));
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
        
    }

    @Override
    public void subscribe(String channel) {
        client.get().subscribe("/topic/" + channel, listener);
    }
}