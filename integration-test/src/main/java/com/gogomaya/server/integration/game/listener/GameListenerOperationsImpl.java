package com.gogomaya.server.integration.game.listener;

import java.io.IOException;
import java.util.Map;

import javax.security.auth.login.LoginException;

import net.ser1.stomp.Client;
import net.ser1.stomp.Listener;

import org.codehaus.jackson.map.ObjectMapper;
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

import com.gogomaya.server.event.GogomayaEvent;
import com.gogomaya.server.game.action.GameState;
import com.gogomaya.server.game.action.GameTable;
import com.gogomaya.server.game.connection.GameServerConnection;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.websocket.DefaultWebSocketListener;
import com.ning.http.client.websocket.WebSocket;
import com.ning.http.client.websocket.WebSocketUpgradeHandler;

public class GameListenerOperationsImpl<State extends GameState> implements GameListenerOperations<State> {

    final private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public GameListenerControl listen(final GameTable<State> gameTable, final GameListener gameListener) {
        // Step 1. Creating Server connection
        // If channel was not defined, choose it randomly
        return listen(gameTable, gameListener, ListenerChannel.values()[(int) (gameTable.getTableId() % ListenerChannel.values().length)]);
    }

    @Override
    public GameListenerControl listen(final GameTable<State> gameTable, final GameListener gameListener, ListenerChannel listenerChannel) {
        // Step 1. Setting default value
        listenerChannel = listenerChannel == null ? ListenerChannel.Rabbit : listenerChannel;
        // Step 2. Processing based on listener type
        switch (listenerChannel) {
        case Rabbit:
            return addRabbitListener(gameTable, gameListener);
        case SockJS:
        case Stomp:
            return addStompListener(gameTable, gameListener);
        default:
            break;
        }

        throw new IllegalArgumentException("Was not able to construct listener");
    }

    private GameListenerControl addRabbitListener(final GameTable<State> gameTable, final GameListener gameListener) {
        // Step 1. Creating Server connection
        GameServerConnection serverConnection = gameTable.getServerResource();
        ConnectionFactory connectionFactory = new CachingConnectionFactory(serverConnection.getNotificationURL());
        // Step 2. Creating binding
        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        Queue tmpQueue = admin.declareQueue();
        Binding tmpBinding = BindingBuilder.bind(tmpQueue).to(new TopicExchange("amq.topic")).with(String.valueOf(gameTable.getTableId()));
        admin.declareBinding(tmpBinding);
        // Step 3. Creating MessageListener
        final SimpleMessageListenerContainer listenerContainer = new SimpleMessageListenerContainer();
        listenerContainer.setConnectionFactory(connectionFactory);
        listenerContainer.setQueues(tmpQueue);
        listenerContainer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                try {
                    System.out.println(new String(message.getBody()));
                    // Step 1. Parsing GameTable
                    GogomayaEvent gameTable = objectMapper.readValue(new String(message.getBody()), GogomayaEvent.class);
                    // Step 2. Updating
                    gameListener.updated(gameTable);
                } catch (Throwable e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        });
        listenerContainer.start();
        // Step 4. Returning game listener with possibility to stop listener
        return new GameListenerControl() {

            @Override
            public void stopListener() {
                listenerContainer.stop();
            }
        };
    }

    private GameListenerControl addStompListener(final GameTable<State> gameTable, final GameListener gameListener) {
        final String channel = "/topic/" + Long.toString(gameTable.getTableId());
        // Step 1. Creating a game table client
        try {
            final Client stompClient = new Client(gameTable.getServerResource().getNotificationURL(), 61613, "guest", "guest");
            stompClient.subscribe("/topic/" + Long.toString(gameTable.getTableId()), new Listener() {

                @Override
                public void message(Map parameters, String message) {
                    try {
                        System.out.println(message);
                        // Step 1. Reading game table
                        GogomayaEvent gameTable = objectMapper.readValue(message, GogomayaEvent.class);
                        // Step 2. Updating game table
                        gameListener.updated(gameTable);
                    } catch (Throwable e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }
            });
            // Step 4. Returning game listener with possibility to stop STOMP listener
            return new GameListenerControl() {

                @Override
                public void stopListener() {
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

    private GameListenerControl addSockJSListener(final GameTable<State> gameTable, final GameListener gameListener) {
        try {
            AsyncHttpClient asyncClient = new AsyncHttpClient();
            final WebSocket webSocket = asyncClient.prepareGet("http://" + gameTable.getServerResource().getNotificationURL() + ":15674/stomp")
                    .execute(new WebSocketUpgradeHandler.Builder().addWebSocketListener(new DefaultWebSocketListener() {

                        @Override
                        public void onOpen(WebSocket websocket) {
                            super.onOpen(webSocket);
                            websocket.sendTextMessage("SUBSCRIBE /topic/" + gameTable.getTableId());
                        }

                        @Override
                        public void onMessage(String message) {
                            super.onMessage(message);
                            try {
                                System.out.println(message);
                                // Step 1. Reading game table
                                GogomayaEvent gameTable = objectMapper.readValue(message, GogomayaEvent.class);
                                // Step 2. Updating game table
                                gameListener.updated(gameTable);
                            } catch (Throwable e) {
                                e.printStackTrace();
                                throw new RuntimeException(e);
                            }
                        }

                    }).build()).get();

            System.out.println(webSocket.isOpen());
            // Step 4. Returning game listener with possibility to stop SOCKJS listener
            return new GameListenerControl() {
                
                @Override
                public void stopListener() {
                    webSocket.close();
                }
            };
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
