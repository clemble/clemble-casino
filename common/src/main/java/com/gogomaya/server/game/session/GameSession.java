package com.gogomaya.server.game.session;

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

import com.gogomaya.server.game.action.GameState;
import com.gogomaya.server.game.table.GameTable;
import com.gogomaya.server.hibernate.JsonHibernateType;

@Entity
@Table(name = "GAME_SESSION")
@TypeDef(name = "gameState", typeClass = JsonHibernateType.class, parameters = {
    @Parameter(name = JsonHibernateType.CLASS_NAME_PARAMETER, value = "com.gogomaya.server.game.action.GameState")
})
public class GameSession implements GameSessionAware<GameSession> {

    /**
     * Generated 16/02/13
     */
    private static final long serialVersionUID = -6572596573895530995L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SESSION_ID")
    @JsonProperty("sessionId")
    private Long sessionId;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "TABLE_ID", nullable = false)
    private GameTable table;

    @Column(name = "SESSION_STATE")
    private GameSessionState sessionState = GameSessionState.Inactive;

    @Column(name = "GAME_STATE", length = 4096)
    @Type(type = "gameState")
    private GameState<?, ?> state;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "GAME_SESSION_PLAYERS", joinColumns = @JoinColumn(name = "SESSION_ID"))
    private Set<Long> players = new HashSet<Long>();

    @Override
    public Long getSessionId() {
        return sessionId;
    }

    @Override
    public GameSession setSessionId(Long newSessionId) {
        this.sessionId = newSessionId;
        return this;
    }

    public GameSessionState getSessionState() {
        return sessionState;
    }

    public void setSessionState(GameSessionState gameSessionState) {
        this.sessionState = gameSessionState;
    }

    public GameState<?, ?> getGameState() {
        return state;
    }

    public void setGameState(GameState<?, ?> gameState) {
        this.state = gameState;
    }

    public Set<Long> getPlayers() {
        return players;
    }

    public void setPlayers(Set<Long> players) {
        this.players = players;
    }

    public void addPlayer(Long player) {
        this.players.add(player);
    }

    public GameTable getTable() {
        return table;
    }

    public void setTable(GameTable table) {
        this.table = table;
    }

}
