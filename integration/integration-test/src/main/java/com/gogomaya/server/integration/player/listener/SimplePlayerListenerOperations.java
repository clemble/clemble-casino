package com.gogomaya.server.integration.player.listener;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.util.Map;

import javax.security.auth.login.LoginException;

import net.ser1.stomp.Client;
import net.ser1.stomp.Listener;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogomaya.configuration.NotificationConfiguration;
import com.gogomaya.configuration.NotificationHost;
import com.gogomaya.event.Event;
import com.gogomaya.player.security.PlayerSession;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.websocket.DefaultWebSocketListener;
import com.ning.http.client.websocket.WebSocket;
import com.ning.http.client.websocket.WebSocketUpgradeHandler;

public class SimplePlayerListenerOperations implements PlayerListenerOperations {

    final private ObjectMapper objectMapper;

    public SimplePlayerListenerOperations(ObjectMapper objectMapper) {
        this.objectMapper = checkNotNull(objectMapper);
    }

    @Override
    public PlayerListenerControl listen(PlayerSession playerSession, final PlayerListener gameListener) {
        // Step 1. Creating Server connection
        return listen(playerSession, gameListener, ListenerChannel.values()[Math.abs(playerSession.getPlayer().hashCode() % ListenerChannel.values().length)]);
    }

    @Override
    public PlayerListenerControl listen(PlayerSession playerSession, final PlayerListener gameListener, ListenerChannel listenerChannel) {
        // Step 1. Setting default value
        listenerChannel = listenerChannel == null ? ListenerChannel.Rabbit : listenerChannel;
        // Step 2. Processing based on listener type
        switch (listenerChannel) {
        case Rabbit:
            return addRabbitListener(playerSession, gameListener);
        case SockJS:
        case Stomp:
            return addStompListener(playerSession, gameListener);
        default:
            break;
        }

        throw new IllegalArgumentException("Was not able to construct listener");
    }

    private PlayerListenerControl addRabbitListener(final PlayerSession playerSession, final PlayerListener playerListener) {
        // Step 1. Creating Server connection
        NotificationHost rabbitNotification = playerSession.getResourceLocations().getNotificationConfiguration().getRabbitHost();
        ConnectionFactory connectionFactory = new CachingConnectionFactory(rabbitNotification.getHost());
        // Step 2. Creating binding
        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        Queue tmpQueue = admin.declareQueue();
        Binding tmpBinding = BindingBuilder.bind(tmpQueue).to(new TopicExchange("amq.topic")).with(playerSession.getPlayer());
        admin.declareBinding(tmpBinding);
        // Step 3. Creating MessageListener
        final SimpleMessageListenerContainer listenerContainer = new SimpleMessageListenerContainer();
        listenerContainer.setConnectionFactory(connectionFactory);
        listenerContainer.setQueues(tmpQueue);
        listenerContainer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                try {
                    System.out.println(playerSession.getPlayer() + " > " + new String(message.getBody()));
                    // Step 1. Parsing GameTable
                    Event event = objectMapper.readValue(new String(message.getBody()), Event.class);
                    // Step 2. Updating game table
                    playerListener.updated(event);
                } catch (Throwable e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        });
        listenerContainer.start();
        // Step 4. Returning game listener with possibility to stop listener
        return new PlayerListenerControl() {

            @Override
            public void close() {
                listenerContainer.stop();
            }
        };
    }

    private PlayerListenerControl addStompListener(final PlayerSession playerSession, final PlayerListener playerListener) {
        final String channel = "/topic/" + playerSession.getPlayer();
        // Step 1. Creating a game table client
        try {
            final NotificationConfiguration notificationConfiguration = playerSession.getResourceLocations().getNotificationConfiguration();
            final NotificationHost stompNotification = playerSession.getResourceLocations().getNotificationConfiguration().getStompHost();
            final Client stompClient = new Client(stompNotification.getHost(), stompNotification.getPort(), notificationConfiguration.getUser(), notificationConfiguration.getPassword());
            stompClient.subscribe(channel, new Listener() {

                @Override
                public void message(Map parameters, String message) {
                    try {
                        System.out.println(playerSession.getPlayer() + " > " + message);
                        // Step 1. Reading game table
                        Event event = objectMapper.readValue(message, Event.class);
                        // Step 2. Updating game table
                        playerListener.updated(event);
                    } catch (Throwable e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }
            });
            // Step 4. Returning game listener with possibility to stop STOMP listener
            return new PlayerListenerControl() {

                @Override
                public void close() {
                    stompClient.unsubscribe(channel);
                    stompClient.disconnect();
                }
            };
        } catch (LoginException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private PlayerListenerControl addSockJSListener(final PlayerSession playerSession, final PlayerListener playerListener) {
        try {
            AsyncHttpClient asyncClient = new AsyncHttpClient();
            NotificationHost sockJSNotification = playerSession.getResourceLocations().getNotificationConfiguration().getSockjsHost();
            final WebSocket webSocket = asyncClient.prepareGet("http://" + sockJSNotification.getHost() + ":" + sockJSNotification.getPort() + "/stomp")
                    .execute(new WebSocketUpgradeHandler.Builder().addWebSocketListener(new DefaultWebSocketListener() {

                        @Override
                        public void onOpen(WebSocket websocket) {
                            super.onOpen(webSocket);
                            websocket.sendTextMessage("SUBSCRIBE /topic/" + playerSession.getPlayer());
                        }

                        @Override
                        public void onMessage(String message) {
                            super.onMessage(message);
                            try {
                                System.out.println(message);
                                // Step 1. Reading game table
                                Event event = objectMapper.readValue(message, Event.class);
                                // Step 2. Updating game table
                                playerListener.updated(event);
                            } catch (Throwable e) {
                                e.printStackTrace();
                                throw new RuntimeException(e);
                            }
                        }

                    }).build()).get();

            System.out.println(webSocket.isOpen());
            // Step 4. Returning game listener with possibility to stop SOCKJS listener
            return new PlayerListenerControl() {

                @Override
                public void close() {
                    webSocket.close();
                }
            };
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
