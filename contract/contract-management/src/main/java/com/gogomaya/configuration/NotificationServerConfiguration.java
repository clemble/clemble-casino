package com.gogomaya.configuration;

import static com.gogomaya.utils.Preconditions.checkNotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class NotificationServerConfiguration {

    final private String user;
    final private String password;
    final private String host;
    final private int port;

    final private String routingKey;

    @JsonCreator
    public NotificationServerConfiguration(@JsonProperty("user") String user,
            @JsonProperty("password") String password,
            @JsonProperty("host") String host,
            @JsonProperty("port") int port,
            @JsonProperty("routingKey") String routingKey) {
        this.user = checkNotNull(user);
        this.password = checkNotNull(password);
        this.host = checkNotNull(host);
        this.port = port;
        this.routingKey = routingKey;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getRoutingKey() {
        return routingKey;
    }

}
