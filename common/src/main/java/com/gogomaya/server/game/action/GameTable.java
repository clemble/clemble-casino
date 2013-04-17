package com.gogomaya.server.game.action;

import java.io.Serializable;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonSubTypes.Type;
import org.codehaus.jackson.annotate.JsonTypeInfo;

import com.gogomaya.server.game.GameSpecification;
import com.gogomaya.server.game.connection.GameServerConnection;
import com.gogomaya.server.game.tictactoe.action.TicTacToeTable;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "game")
@JsonSubTypes({ @Type(value = TicTacToeTable.class, name = "ticTacToe") })
public interface GameTable<S extends GameSession<?>> extends Serializable {

    public long getTableId();

    public GameServerConnection getServerResource();

    public Set<Long> getPlayers();

    public void addPlayer(long player);

    public S getCurrentSession();

    public GameSpecification getSpecification();

    public GameState<?, ?> getState();

}
