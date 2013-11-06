package com.clemble.casino.player.client;

public class ClientDetails implements ClientAware {

    final private String client;

    public ClientDetails(String client) {
        this.client = client;
    }

    @Override
    public String getClient() {
        return client;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((client == null) ? 0 : client.hashCode());
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
        if (client == null) {
            if (other.client != null)
                return false;
        } else if (!client.equals(other.client))
            return false;
        return true;
    }

}
