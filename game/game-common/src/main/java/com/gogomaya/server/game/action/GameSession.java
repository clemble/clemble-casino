package com.gogomaya.server.game.action;

import java.io.Serializable;
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
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.TypeDef;

import com.gogomaya.server.game.action.move.GameMove;
import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.game.tictactoe.action.TicTacToeState;
import com.gogomaya.server.hibernate.JsonHibernateType;

@Entity
@Table(name = "GAME_SESSION")
@TypeDef(name = "gameState", typeClass = JsonHibernateType.class, defaultForType = TicTacToeState.class, parameters = { @Parameter(
        name = JsonHibernateType.CLASS_NAME_PARAMETER,
        value = "com.gogomaya.server.game.action.GameState") })
public class GameSession<State extends GameState> implements Serializable {

    /**
     * Generated 16/02/13
     */
    private static final long serialVersionUID = -6572596573895530995L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SESSION_ID")
    @JsonProperty("sessionId")
    private long sessionId;

    @ManyToOne
    @JoinColumns(value = { @JoinColumn(name = "SPECIFICATION_NAME", referencedColumnName = "SPECIFICATION_NAME"),
            @JoinColumn(name = "SPECIFICATION_GROUP", referencedColumnName = "SPECIFICATION_GROUP") })
    private GameSpecification specification;

    @Column(name = "SESSION_STATE")
    private GameSessionState sessionState;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "TIC_TAC_TOE_SESSION_PLAYERS", joinColumns = @JoinColumn(name = "SESSION_ID"))
    private Set<Long> players = new HashSet<Long>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "TIC_TAC_TOE_SESSION_MOVES", joinColumns = @JoinColumn(name = "SESSION_ID"))
    private Set<GameMove> madeMoves = new HashSet<GameMove>();

    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long newSessionId) {
        this.sessionId = newSessionId;
    }

    public GameSpecification getSpecification() {
        return specification;
    }

    public void setSpecification(GameSpecification specification) {
        this.specification = specification;
    }

    public GameSessionState getSessionState() {
        return sessionState;
    }

    public void setSessionState(GameSessionState gameSessionState) {
        this.sessionState = gameSessionState;
    }

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

    public Set<GameMove> getMadeMoves() {
        return madeMoves;
    }

    public void addMadeMove(GameMove madeMove) {
        this.madeMoves.add(madeMove);
    }

    public void addMadeMoves(Collection<GameMove> madeMoves) {
        this.madeMoves.addAll(madeMoves);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((madeMoves == null) ? 0 : madeMoves.hashCode());
        result = prime * result + ((players == null) ? 0 : players.hashCode());
        result = prime * result + (int) (sessionId ^ (sessionId >>> 32));
        result = prime * result + ((sessionState == null) ? 0 : sessionState.hashCode());
        result = prime * result + ((specification == null) ? 0 : specification.hashCode());
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GameSession<State> other = (GameSession<State>) obj;
        if (madeMoves == null) {
            if (other.madeMoves != null)
                return false;
        } else if (!madeMoves.equals(other.madeMoves))
            return false;
        if (players == null) {
            if (other.players != null)
                return false;
        } else if (!players.equals(other.players))
            return false;
        if (sessionId != other.sessionId)
            return false;
        if (sessionState != other.sessionState)
            return false;
        if (specification == null) {
            if (other.specification != null)
                return false;
        } else if (!specification.equals(other.specification))
            return false;
        return true;
    }

}
