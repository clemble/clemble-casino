package com.gogomaya.server.game.iterator;

import java.io.Serializable;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
public interface GamePlayerIterator extends Serializable {

    public long next();

    public long current();

    public Collection<Long> getPlayers();

    public Collection<Long> whoIsOpponents(long playerId);

    public boolean contains(long playerId);

}
