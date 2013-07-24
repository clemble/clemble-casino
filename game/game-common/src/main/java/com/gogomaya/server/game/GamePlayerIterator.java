package com.gogomaya.server.game;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
public interface GamePlayerIterator extends Serializable {

    public long next();

    public long current();

    public long[] getPlayers();

    public boolean contains(long playerId);

}
