package com.gogomaya.server.game.action;

import java.io.Serializable;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonSubTypes.Type;
import org.codehaus.jackson.annotate.JsonTypeInfo;

import com.gogomaya.server.game.connection.GameServerConnection;
import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.game.tictactoe.action.TicTacToeTable;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "game")
@JsonSubTypes({ @Type(value = TicTacToeTable.class, name = "ticTacToe") })
public interface GameTable<State extends GameState> extends Serializable {

    public long getTableId();

    public GameServerConnection getServerResource();

    public GameTable<State> setServerResource(GameServerConnection serverConnection);

    public GameSpecification getSpecification();

    public GameTable<State> setSpecification(GameSpecification specification);

    public Set<Long> getPlayers();

    public void addPlayer(long player);

    public GameSession<State> getCurrentSession();

    public void setCurrentSession(GameSession<State> newSession);

    public State getState();

    public GameTable<State> setState(State state);

}
