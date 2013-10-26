package com.clemble.casino.configuration;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((password == null) ? 0 : password.hashCode());
        result = prime * result + ((rabbitHost == null) ? 0 : rabbitHost.hashCode());
        result = prime * result + ((routingKey == null) ? 0 : routingKey.hashCode());
        result = prime * result + ((sockjsHost == null) ? 0 : sockjsHost.hashCode());
        result = prime * result + ((stompHost == null) ? 0 : stompHost.hashCode());
        result = prime * result + ((user == null) ? 0 : user.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        NotificationConfiguration other = (NotificationConfiguration) obj;
        if (password == null) {
            if (other.password != null)
                return false;
        } else if (!password.equals(other.password))
            return false;
        if (rabbitHost == null) {
            if (other.rabbitHost != null)
                return false;
        } else if (!rabbitHost.equals(other.rabbitHost))
            return false;
        if (routingKey == null) {
            if (other.routingKey != null)
                return false;
        } else if (!routingKey.equals(other.routingKey))
            return false;
        if (sockjsHost == null) {
            if (other.sockjsHost != null)
                return false;
        } else if (!sockjsHost.equals(other.sockjsHost))
            return false;
        if (stompHost == null) {
            if (other.stompHost != null)
                return false;
        } else if (!stompHost.equals(other.stompHost))
            return false;
        if (user == null) {
            if (other.user != null)
                return false;
        } else if (!user.equals(other.user))
            return false;
        return true;
    }

}
