package com.gogomaya.server.game.action;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonSubTypes.Type;
import org.codehaus.jackson.annotate.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @Type(value = SequentialPlayerIterator.class, name = "sequential") })
public interface GamePlayerIterator extends Serializable {

    public long next();

    public long current();

    public long[] getPlayers();

    public boolean contains(long playerId);

    public GamePlayerIterator remove(long playerId);

}
