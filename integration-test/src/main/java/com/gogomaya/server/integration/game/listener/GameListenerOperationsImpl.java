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

import com.gogomaya.server.game.action.GameTable;
import com.gogomaya.server.game.connection.GameServerConnection;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.websocket.DefaultWebSocketListener;
import com.ning.http.client.websocket.WebSocket;
import com.ning.http.client.websocket.WebSocketUpgradeHandler;

public class GameListenerOperationsImpl<T extends GameTable<?>> implements GameListenerOperations<T>{
    
    final private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void listen(final T gameTable, final GameListener<T> gameListener) {
        // Step 1. Creating Server connection
        // If channel was not defined, choose it randomly
        listen(gameTable, gameListener, ListenerChannel.values()[(int) (gameTable.getTableId() % ListenerChannel.values().length)]);
    }

    @Override
    public void listen(final T gameTable, final GameListener<T> gameListener, ListenerChannel listenerChannel) {
        // Step 1. Setting default value
        listenerChannel = listenerChannel == null ? ListenerChannel.Rabbit : listenerChannel;
        // Step 2. Processing based on listener type
        switch (listenerChannel) {
        case Rabbit:
            addRabbitListener(gameTable, gameListener);
            break;
        case SockJS:
        case Stomp:
            addStompListener(gameTable, gameListener);
            break;
        default:
            break;
        }

    }

    private void addRabbitListener(final T gameTable, final GameListener<T> gameListener) {
        // Step 1. Creating Server connection
        GameServerConnection serverConnection = gameTable.getServerResource();
        ConnectionFactory connectionFactory = new CachingConnectionFactory(serverConnection.getNotificationURL());
        // Step 2. Creating binding
        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        Queue tmpQueue = admin.declareQueue();
        Binding tmpBinding = BindingBuilder.bind(tmpQueue).to(new TopicExchange("amq.topic")).with(String.valueOf(gameTable.getTableId()));
        admin.declareBinding(tmpBinding);
        // Step 3. Creating MessageListener
        SimpleMessageListenerContainer listenerContainer = new SimpleMessageListenerContainer();
        listenerContainer.setConnectionFactory(connectionFactory);
        listenerContainer.setQueues(tmpQueue);
        listenerContainer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                try {
                    // Step 1. Parsing GameTable
                    T gameTable = (T) objectMapper.readValue(new String(message.getBody()), GameTable.class);
                    // Step 2. Updating
                    gameListener.updated(gameTable);
                } catch (Throwable e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        });
        listenerContainer.start();
    }

    private void addStompListener(final T gameTable, final GameListener<T> gameListener) {
        // Step 1. Creating a game table client
        Client c;
        try {
            c = new Client(gameTable.getServerResource().getNotificationURL(), 61613, "guest", "guest");
            c.subscribe("/topic/" + Long.toString(gameTable.getTableId()), new Listener() {

                @Override
                public void message(Map arg0, String arg1) {
                    try {
                        // Step 1. Reading game table
                        T gameTable = (T) objectMapper.readValue(arg1, GameTable.class);
                        // Step 2. Updating game table
                        gameListener.updated(gameTable);
                    } catch (Throwable e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }
            });
        } catch (LoginException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void addSockJSListener(final T gameTable, final GameListener<T> gameListener) {
        AsyncHttpClient asyncClient = new AsyncHttpClient();
        try {
            WebSocket webSocket = asyncClient.prepareGet("http://" + gameTable.getServerResource().getNotificationURL() + ":15674/stomp")
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
                                // Step 1. Reading game table
                                T gameTable = (T) objectMapper.readValue(message, GameTable.class);
                                // Step 2. Updating game table
                                gameListener.updated(gameTable);
                            } catch (Throwable e) {
                                e.printStackTrace();
                                throw new RuntimeException(e);
                            }
                        }

                    }).build()).get();

            System.out.println(webSocket.isOpen());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
