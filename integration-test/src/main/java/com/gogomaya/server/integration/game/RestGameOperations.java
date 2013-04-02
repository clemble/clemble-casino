package com.gogomaya.server.integration.game;

import static com.google.common.base.Preconditions.checkNotNull;

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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.gogomaya.server.game.GameSpecification;
import com.gogomaya.server.game.connection.GameServerConnection;
import com.gogomaya.server.game.table.GameTable;
import com.gogomaya.server.integration.player.Player;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.websocket.DefaultWebSocketListener;
import com.ning.http.client.websocket.WebSocket;
import com.ning.http.client.websocket.WebSocketUpgradeHandler;

public class RestGameOperations implements GameOperations {

    final private static String CREATE_URL = "/spi/active/session";

    final private RestTemplate restTemplate;
    final private String baseUrl;

    final private ObjectMapper objectMapper = new ObjectMapper();

    public RestGameOperations(final String baseUrl, final RestTemplate restTemplate) {
        this.restTemplate = checkNotNull(restTemplate);
        this.baseUrl = checkNotNull(baseUrl);
    }

    @Override
    public GameTable create(Player player) {
        return create(player, null);
    }

    @Override
    public GameTable create(Player player, GameSpecification gameSpecification) {
        gameSpecification = gameSpecification == null ? GameSpecification.DEFAULT_SPECIFICATION : gameSpecification;
        // Step 1. Initializing headers
        MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
        header.add("playerId", String.valueOf(player.getPlayerId()));
        header.add("Content-Type", "application/json");
        // Step 2. Generating request
        HttpEntity<GameSpecification> requestEntity = new HttpEntity<GameSpecification>(gameSpecification, header);
        // Step 3. Rest template generation
        return restTemplate.exchange(baseUrl + CREATE_URL, HttpMethod.POST, requestEntity, GameTable.class).getBody();
    }

    @Override
    public void addListener(final GameTable gameTable, final GameListener gameListener) {
        // Step 1. Creating Server connection
        addListener(gameTable, gameListener, ListenerChannel.Rabbit);
    }

    @Override
    public void addListener(final GameTable gameTable, final GameListener gameListener, ListenerChannel listenerChannel) {
        // Step 1. Setting default value
        listenerChannel = listenerChannel == null ? ListenerChannel.Rabbit : listenerChannel;
        // Step 2. Processing based on listener type
        switch (listenerChannel) {
        case Rabbit:
            addRabbitListener(gameTable, gameListener);
            break;
        case SockJS:
            addSockJSListener(gameTable, gameListener);
            break;
        case Stomp:
            addStompListener(gameTable, gameListener);
            break;
        default:
            break;
        }

    }

    private void addRabbitListener(final GameTable gameTable, final GameListener gameListener) {
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
                    System.out.println("Rabbit message received");
                    // Step 1. Parsing GameTable
                    GameTable gameTable = objectMapper.readValue(new String(message.getBody()), GameTable.class);
                    // Step 2. Updating
                    gameListener.updated(gameTable);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        listenerContainer.start();
    }

    private void addStompListener(final GameTable gameTable, final GameListener gameListener) {
        // Step 1. Creating a game table client
        Client c;
        try {
            c = new Client(gameTable.getServerResource().getNotificationURL(), 61613, "guest", "guest");
            c.subscribe("/topic/" + Long.toString(gameTable.getTableId()), new Listener() {

                @Override
                public void message(Map arg0, String arg1) {
                    System.out.println("Stomp message received");
                    try {
                        // Step 1. Creating game table
                        GameTable gameTable = objectMapper.readValue(arg1, GameTable.class);
                        // Step 2. Updating game table
                        gameListener.updated(gameTable);
                    } catch (Exception e) {
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

    private void addSockJSListener(final GameTable gameTable, final GameListener gameListener) {
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
                                GameTable gameTable = objectMapper.readValue(message, GameTable.class);
                                gameListener.updated(gameTable);
                            } catch (Exception e) {
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
