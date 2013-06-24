package com.gogomaya.server.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.gogomaya.server.event.ClientEvent;
import com.gogomaya.server.game.construct.GameRequest;
import com.gogomaya.server.game.event.client.MadeMove;
import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.game.specification.GameSpecificationAware;
import com.gogomaya.server.hibernate.JsonHibernateType;

@Entity
@Table(name = "GAME_SESSION")
@TypeDefs(value = { @TypeDef(name = "gameState", typeClass = JsonHibernateType.class, defaultForType = GameState.class, parameters = { @Parameter(name = JsonHibernateType.CLASS_NAME_PARAMETER, value = "com.gogomaya.server.game.GameState") }) })
public class GameSession<State extends GameState> implements GameSpecificationAware, SessionAware, Serializable {

    /**
     * Generated 16/02/13
     */
    private static final long serialVersionUID = -6572596573895530995L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SESSION_ID")
    private long session;

    @ManyToOne
    @JoinColumns(value = { @JoinColumn(name = "SPECIFICATION_NAME", referencedColumnName = "SPECIFICATION_NAME"),
            @JoinColumn(name = "SPECIFICATION_GROUP", referencedColumnName = "SPECIFICATION_GROUP") })
    private GameSpecification specification;

    @Transient
    private GameRequest constructor;

    @Column(name = "SESSION_STATE")
    private GameSessionState sessionState;

    @ElementCollection(fetch = FetchType.EAGER)
    @OrderColumn(name = "PLAYERS_ORDER")
    @CollectionTable(name = "GAME_SESSION_PLAYERS", joinColumns = @JoinColumn(name = "SESSION_ID"))
    private List<Long> players = new ArrayList<Long>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "GAME_SESSION_MOVES", joinColumns = @JoinColumn(name = "SESSION_ID"))
    private List<MadeMove> madeMoves = new ArrayList<MadeMove>();

    @Type(type = "gameState")
    @Column(name = "GAME_STATE", length = 4096)
    private State state;

    @Column(name = "NUM_MADE_MOVES")
    private int numMadeMoves;

    @Override
    public long getSession() {
        return session;
    }

    public void setSession(long newSession) {
        this.session = newSession;
    }

    @Override
    public GameSpecification getSpecification() {
        return specification;
    }

    public GameSession<State> setSpecification(GameSpecification specification) {
        this.specification = specification;
        return this;
    }

    public GameSessionState getSessionState() {
        return sessionState;
    }

    public void setSessionState(GameSessionState gameSessionState) {
        this.sessionState = gameSessionState;
    }

    public List<Long> getPlayers() {
        return players;
    }

    public GameSession<State> setPlayers(Collection<Long> players) {
        this.players.addAll(players);
        return this;
    }

    public void addPlayer(Long player) {
        this.players.add(player);
    }

    public void addPlayers(Collection<Long> players) {
        this.players.addAll(players);
    }

    public List<MadeMove> getMadeMoves() {
        return madeMoves;
    }

    public void addMadeMove(ClientEvent madeMove) {
        MadeMove move = new MadeMove().setMove(madeMove).setMoveId(numMadeMoves++).setProcessingTime(System.currentTimeMillis());
        this.madeMoves.add(move);
    }

    public int getNumMadeMoves() {
        return numMadeMoves;
    }

    public void setNumMadeMoves(int numMadeMoves) {
        this.numMadeMoves = numMadeMoves;
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
    @JsonSubTypes({ @com.fasterxml.jackson.annotation.JsonSubTypes.Type(value = GameState.class, name = "ticTacToe") })
    public State getState() {
        return state;
    }

    public GameSession<State> setState(State state) {
        this.state = state;
        return this;
    }

    public GameRequest getConstructor() {
        return constructor;
    }

    public GameSession<State> setConstructor(GameRequest constructor) {
        this.constructor = constructor;
        return this;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((madeMoves == null) ? 0 : madeMoves.hashCode());
        result = prime * result + ((players == null) ? 0 : players.hashCode());
        result = prime * result + (int) (session ^ (session >>> 32));
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
        if (session != other.session)
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
