package com.gogomaya.server.game.action;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonSubTypes.Type;
import org.codehaus.jackson.annotate.JsonTypeInfo;

import com.gogomaya.server.game.tictactoe.TicTacToePlayerIterator;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @Type(value = TicTacToePlayerIterator.class, name = "sequential") })
public interface GamePlayerIterator extends Serializable {

    public long next();

    public long current();

    public long[] getPlayers();

}
