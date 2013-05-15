package com.gogomaya.server.game.action;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @Type(value = SequentialPlayerIterator.class, name = "sequential") })
public interface GamePlayerIterator extends Serializable {

    public long next();

    public long current();

    public long[] getPlayers();

    public boolean contains(long playerId);

    public GamePlayerIterator remove(long playerId);

}
