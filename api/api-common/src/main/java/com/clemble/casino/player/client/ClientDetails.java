package com.clemble.casino.player.client;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ClientDetails implements ConsumerAware {

    @Id
    final private String consumerName;

    @JsonCreator
    public ClientDetails(@JsonProperty("consumerName") String consumerName) {
        this.consumerName = consumerName;
    }

    @Override
    public String getConsumerName() {
        return consumerName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((consumerName == null) ? 0 : consumerName.hashCode());
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
        ClientDetails other = (ClientDetails) obj;
        if (consumerName == null) {
            if (other.consumerName != null)
                return false;
        } else if (!consumerName.equals(other.consumerName))
            return false;
        return true;
    }

}
