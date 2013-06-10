package com.gogomaya.server.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
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
import javax.persistence.OneToOne;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.game.specification.GameSpecificationAware;

@Entity
@Table(name = "GAME_TABLE")
public class GameTable<State extends GameState> implements GameSpecificationAware, Serializable {

    /**
     * Generated 27/03/13
     */
    private static final long serialVersionUID = 7478219659169235161L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TABLE_ID")
    private long tableId;

    @ManyToOne
    @JoinColumns(value = { @JoinColumn(name = "SPECIFICATION_NAME", referencedColumnName = "SPECIFICATION_NAME"),
            @JoinColumn(name = "SPECIFICATION_GROUP", referencedColumnName = "SPECIFICATION_GROUP") })
    private GameSpecification specification;

    @ElementCollection(fetch = FetchType.EAGER)
    @OrderColumn(name = "PLAYERS_ORDER")
    @CollectionTable(name = "GAME_TABLE_PLAYERS", joinColumns = @JoinColumn(name = "TABLE_ID"))
    private List<Long> players = new ArrayList<Long>();

    @OneToOne(fetch = FetchType.EAGER, targetEntity = GameSession.class, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "SESSION_ID")
    private GameSession<State> currentSession;

    @Transient
    private String server;

    public long getTableId() {
        return tableId;
    }

    public GameTable<State> setTableId(long tableId) {
        this.tableId = tableId;
        return this;
    }

    public List<Long> getPlayers() {
        return players;
    }

    public GameTable<State> setPlayers(List<Long> players) {
        this.players = players;
        return this;
    }

    public GameTable<State> addPlayer(long player) {
        this.players.add(player);
        if (currentSession != null)
            currentSession.addPlayer(player);
        return this;
    }

    public GameSession<State> getCurrentSession() {
        return currentSession;
    }

    public GameTable<State> setCurrentSession(GameSession<State> currentSession) {
        this.currentSession = currentSession;
        if (currentSession != null) {
            this.currentSession.setSpecification(getSpecification());
            this.currentSession.setPlayers(getPlayers());
        }
        return this;
    }

    @Override
    public GameSpecification getSpecification() {
        return specification;
    }

    public GameTable<State> setSpecification(GameSpecification specification) {
        this.specification = specification;
        if (this.currentSession != null)
            this.currentSession.setSpecification(specification);
        return this;
    }

    public void clear() {
        this.currentSession = null;
        this.players.clear();
    }

    public String getServer() {
        return server;
    }

    public GameTable<State> setServer(String server) {
        this.server = server;
        return this;
    }

}
