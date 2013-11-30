package com.clemble.casino.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.Type;

import com.clemble.casino.VersionAware;
import com.clemble.casino.event.ClientEvent;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.game.event.client.MadeMove;
import com.clemble.casino.game.specification.GameSpecification;
import com.clemble.casino.game.specification.GameSpecificationAware;

@Entity
@Table(name = "GAME_SESSION")
public class GameSession<State extends GameState> implements GameSpecificationAware, GameSessionAware, VersionAware, Serializable {

    /**
     * Generated 16/02/13
     */
    private static final long serialVersionUID = -6572596573895530995L;

    @EmbeddedId
    private GameSessionKey session;

    @ManyToOne
    @JoinColumns(value = {
            @JoinColumn(name = "SPECIFICATION_NAME", referencedColumnName = "SPECIFICATION_NAME"),
            @JoinColumn(name = "GAME_NAME", referencedColumnName = "GAME_NAME") })
    private GameSpecification specification;

    @Column(name = "SESSION_STATE")
    private GameSessionState sessionState;

    @ElementCollection(fetch = FetchType.EAGER)
    @OrderColumn(name = "PLAYERS_ORDER")
    @CollectionTable(name = "GAME_SESSION_PLAYERS",
            joinColumns = {@JoinColumn(name = "SESSION_ID"), @JoinColumn(name = "GAME")})
    private List<String> players = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "GAME_SESSION_MOVES", 
        joinColumns = {@JoinColumn(name = "SESSION_ID"), @JoinColumn(name = "GAME")})
    private List<MadeMove> madeMoves = new ArrayList<MadeMove>();

    @Type(type = "com.clemble.casino.game.GameStateHibernate")
    @Column(name = "GAME_STATE", length = 4096)
    private State state;

    @Version
    @Column(name = "VERSION")
    private int version;

    public GameSession() {
    }

    @Override
    public GameSessionKey getSession() {
        return session;
    }

    public void setSession(GameSessionKey newSession) {
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

    public List<String> getPlayers() {
        return players;
    }

    public GameSession<State> setPlayers(Collection<String> players) {
        this.players.clear();
        this.players.addAll(players);
        return this;
    }

    public void addPlayer(String player) {
        this.players.add(player);
    }

    public void addPlayers(Collection<String> players) {
        this.players.addAll(players);
    }

    public List<MadeMove> getMadeMoves() {
        return madeMoves;
    }

    public void addMadeMove(ClientEvent madeMove) {
        MadeMove move = new MadeMove().setMove(madeMove).setMoveId(version + 1).setProcessingTime(System.currentTimeMillis());
        this.madeMoves.add(move);
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public State getState() {
        return state;
    }

    public GameSession<State> setState(State state) {
        this.state = state;
        return this;
    }

    public GameInitiation toInitiation() {
        return new GameInitiation(session, players, specification);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((madeMoves == null) ? 0 : madeMoves.hashCode());
        result = prime * result + ((players == null) ? 0 : players.hashCode());
        result = prime * result + ((session == null) ? 0 : session.hashCode());
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
