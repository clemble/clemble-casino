package com.gogomaya.configuration;

import static com.gogomaya.utils.Preconditions.checkNotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class NotificationConfiguration {

    final private String user;
    final private String password;

    final private String routingKey;

    final private NotificationHost rabbitHost;
    final private NotificationHost stompHost;
    final private NotificationHost sockjsHost;

    @JsonCreator
    public NotificationConfiguration(@JsonProperty("user") String user,
            @JsonProperty("password") String password,
            @JsonProperty("routingKey") String routingKey,
            @JsonProperty("rabbitHost") NotificationHost rabbitHost,
            @JsonProperty("stompHost") NotificationHost stompHost,
            @JsonProperty("sockjsHost") NotificationHost sockjsHost) {
        this.user = checkNotNull(user);
        this.password = checkNotNull(password);
        this.routingKey = checkNotNull(routingKey);
        this.rabbitHost = checkNotNull(rabbitHost);
        this.stompHost = checkNotNull(stompHost);
        this.sockjsHost = checkNotNull(sockjsHost);
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public NotificationHost getRabbitHost() {
        return rabbitHost;
    }

    public NotificationHost getStompHost() {
        return stompHost;
    }

    public NotificationHost getSockjsHost() {
        return sockjsHost;
    }

}
