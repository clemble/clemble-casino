package com.gogomaya.server.game.tictactoe;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.gogomaya.server.game.action.GameSession;
import com.gogomaya.server.game.action.GameSessionState;
import com.gogomaya.server.game.tictactoe.action.TicTacToeState;
import com.gogomaya.server.game.tictactoe.action.TicTacToeTable;
import com.gogomaya.server.hibernate.JsonHibernateType;

@Entity
@Table(name = "TIC_TAC_TOE_SESSION")
@TypeDef(name = "gameState", typeClass = JsonHibernateType.class, defaultForType = TicTacToeState.class, parameters = { @Parameter(
        name = JsonHibernateType.CLASS_NAME_PARAMETER,
        value = "com.gogomaya.server.game.action.GameState") })
public class TicTacToeSession implements GameSession<TicTacToeState> {

    /**
     * Generated 16/02/13
     */
    private static final long serialVersionUID = -6572596573895530995L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SESSION_ID")
    @JsonProperty("sessionId")
    private long sessionId;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "TABLE_ID", nullable = false)
    private TicTacToeTable table;

    @Column(name = "SESSION_STATE")
    private GameSessionState sessionState;

    @Type(type = "gameState")
    @Column(name = "GAME_STATE", length = 4096)
    private TicTacToeState state;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "TIC_TAC_TOE_SESSION_PLAYERS", joinColumns = @JoinColumn(name = "SESSION_ID"))
    private Set<Long> players = new HashSet<Long>();

    @Override
    public long getSessionId() {
        return sessionId;
    }

    public TicTacToeSession setSessionId(long newSessionId) {
        this.sessionId = newSessionId;
        return this;
    }

    public TicTacToeSession setTable(TicTacToeTable table) {
        this.table = table;
        return this;
    }

    @Override
    public GameSessionState getSessionState() {
        return sessionState;
    }

    public void setSessionState(GameSessionState gameSessionState) {
        this.sessionState = gameSessionState;
    }

    @Override
    public TicTacToeState getGameState() {
        return state;
    }

    public void setGameState(TicTacToeState gameState) {
        this.state = gameState;
    }

    @Override
    public Set<Long> getPlayers() {
        return players;
    }

    public void setPlayers(Set<Long> players) {
        this.players = players;
    }

    public void addPlayers(Collection<Long> players) {
        if (this.players == null)
            this.players = new HashSet<Long>();
        this.players.addAll(players);
    }

    @Override
    public TicTacToeTable getTable() {
        return table;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((players == null) ? 0 : players.hashCode());
        result = prime * result + (int) (sessionId ^ (sessionId >>> 32));
        result = prime * result + ((sessionState == null) ? 0 : sessionState.hashCode());
        result = prime * result + ((state == null) ? 0 : state.hashCode());
        result = prime * result + ((table == null) ? 0 : table.hashCode());
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
        TicTacToeSession other = (TicTacToeSession) obj;
        if (players == null) {
            if (other.players != null)
                return false;
        } else if (!players.equals(other.players))
            return false;
        if (sessionId != other.sessionId)
            return false;
        if (sessionState != other.sessionState)
            return false;
        if (state == null) {
            if (other.state != null)
                return false;
        } else if (!state.equals(other.state))
            return false;
        if (table == null) {
            if (other.table != null)
                return false;
        } else if (!table.equals(other.table))
            return false;
        return true;
    }

}
