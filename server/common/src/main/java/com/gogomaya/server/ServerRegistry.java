package com.gogomaya.server;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ServerRegistry implements Serializable {

    /**
     * Server registry
     */
    private static final long serialVersionUID = 8743316420554283274L;

    final private TreeMap<Long, String> SERVER_REGISTRY = new TreeMap<Long, String>();

    public ServerRegistry() {
    }

    @JsonCreator
    public ServerRegistry(@JsonProperty("range") Collection<Entry<String, String>> ranges) {
        if (ranges == null)
            return;
        for (Entry<String, String> range : ranges)
            register(Long.valueOf(range.getKey()), range.getValue());

    }

    public void register(Long lastId, String host) {
        SERVER_REGISTRY.put(lastId, host);
    }

    public Collection<Entry<Long, String>> getRange() {
        return SERVER_REGISTRY.entrySet();
    }

    public String find(Long id) {
        // Step 1. Search for closest floor server connection
        Entry<Long, String> connectionEntry = SERVER_REGISTRY.ceilingEntry(id);
        if (connectionEntry != null)
            return connectionEntry.getValue();
        // Step 2. Search for closest lower server connection
        connectionEntry = SERVER_REGISTRY.floorEntry(id);
        return connectionEntry != null ? connectionEntry.getValue() : null;
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
