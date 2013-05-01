package com.gogomaya.server.game.action;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.gogomaya.server.game.connection.GameServerConnection;
import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.game.tictactoe.action.TicTacToeState;
import com.gogomaya.server.hibernate.JsonHibernateType;

@Entity
@Table(name = "GAME_TABLE")
@TypeDef(name = "gameState", typeClass = JsonHibernateType.class, defaultForType = TicTacToeState.class, parameters = { @Parameter(
        name = JsonHibernateType.CLASS_NAME_PARAMETER,
        value = "com.gogomaya.server.game.action.GameState") })
public class GameTable<State extends GameState> implements Serializable {

    /**
     * Generated 27/03/13
     */
    private static final long serialVersionUID = 7478219659169235161L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TABLE_ID")
    @JsonProperty("tableId")
    private long tableId;

    @Embedded
    private GameServerConnection serverResource;

    @ManyToOne
    @JoinColumns(value = { @JoinColumn(name = "SPECIFICATION_NAME", referencedColumnName = "SPECIFICATION_NAME"),
            @JoinColumn(name = "SPECIFICATION_GROUP", referencedColumnName = "SPECIFICATION_GROUP") })
    private GameSpecification specification;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "TIC_TAC_TOE_TABLE_PLAYERS", joinColumns = @JoinColumn(name = "TABLE_ID"))
    private Set<Long> players = new HashSet<Long>();

    @OneToOne(fetch = FetchType.EAGER, targetEntity = GameSession.class)
    @JoinColumn(name = "SESSION_ID")
    private GameSession currentSession;

    @Type(type = "gameState")
    @Column(name = "GAME_STATE", length = 4096)
    private State state;

    public long getTableId() {
        return tableId;
    }

    public GameTable<State> setTableId(long tableId) {
        this.tableId = tableId;
        return this;
    }

    public GameServerConnection getServerResource() {
        return serverResource;
    }

    public GameTable<State> setServerResource(GameServerConnection serverResource) {
        this.serverResource = serverResource;
        return this;
    }

    public Set<Long> getPlayers() {
        return players;
    }

    public GameTable<State> setPlayers(Set<Long> players) {
        this.players = players;
        return this;
    }

    public GameTable<State> addPlayer(long player) {
        this.players.add(player);
        return this;
    }

    public GameSession getCurrentSession() {
        return currentSession;
    }

    public GameTable<State> setCurrentSession(GameSession currentSession) {
        this.currentSession = currentSession;
        return this;
    }

    public GameSpecification getSpecification() {
        return specification;
    }

    public GameTable<State> setSpecification(GameSpecification specification) {
        this.specification = specification;
        return this;
    }

    public State getState() {
        return state;
    }

    public GameTable<State> setState(State state) {
        this.state = state;
        return this;
    }

}
