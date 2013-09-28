package com.gogomaya.server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ServerRegistry implements Serializable {

    /**
     * Server registry
     */
    private static final long serialVersionUID = 8743316420554283274L;

    final private List<String> SERVER_REGISTRY;

    @JsonCreator
    public ServerRegistry(@JsonProperty("range") List<String> servers) {
        SERVER_REGISTRY = new ArrayList<>(servers);
    }

    public List<String> getRange() {
        return SERVER_REGISTRY;
    }

    public String find(String id) {
        // Step 1. Get server index
        int hashCode = id.hashCode() % SERVER_REGISTRY.size();
        // Step 2. Search for closest lower server connection
        return SERVER_REGISTRY.get(hashCode);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((SERVER_REGISTRY == null) ? 0 : SERVER_REGISTRY.hashCode());
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
        ServerRegistry other = (ServerRegistry) obj;
        if (SERVER_REGISTRY == null) {
            if (other.SERVER_REGISTRY != null)
                return false;
        } else if (!SERVER_REGISTRY.equals(other.SERVER_REGISTRY))
            return false;
        return true;
    }

}
