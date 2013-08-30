package com.gogomaya.game;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ServerResourse {

    final private String server;

    final private long tableId;

    @JsonCreator
    public ServerResourse(@JsonProperty("server") final String server, @JsonProperty("tableId") final long tableId) {
        this.server = server;
        this.tableId = tableId;
    }

    public String getServer() {
        return server;
    }

    public long getTableId() {
        return tableId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((server == null) ? 0 : server.hashCode());
        result = prime * result + (int) (tableId ^ (tableId >>> 32));
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
        ServerResourse other = (ServerResourse) obj;
        if (server == null) {
            if (other.server != null)
                return false;
        } else if (!server.equals(other.server))
            return false;
        if (tableId != other.tableId)
            return false;
        return true;
    }

}
