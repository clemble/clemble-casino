package com.gogomaya.server.game;

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
}
