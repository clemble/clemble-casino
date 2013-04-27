package com.gogomaya.server.game.action;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonSubTypes.Type;
import org.codehaus.jackson.annotate.JsonTypeInfo;

import com.gogomaya.server.game.action.move.GameMove;
import com.gogomaya.server.game.tictactoe.TicTacToeSession;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
    @Type(value = TicTacToeSession.class, name = "ticTacToe"), 
})
public interface GameSession<State extends GameState> extends Serializable {

    public long getSessionId();

    public GameTable<State> getTable();

    public void setTable(GameTable<State> table);

    public Set<Long> getPlayers();

    public void addPlayers(Collection<Long> players);

    public GameSessionState getSessionState();

    public void setSessionState(GameSessionState gameSessionState);

    public List<GameMove> getMadeMoves();

}
