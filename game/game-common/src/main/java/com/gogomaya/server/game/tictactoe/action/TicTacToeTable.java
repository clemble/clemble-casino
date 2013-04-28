package com.gogomaya.server.game.tictactoe.action;

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

import com.gogomaya.server.game.action.GameSession;
import com.gogomaya.server.game.action.GameTable;
import com.gogomaya.server.game.connection.GameServerConnection;
import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.hibernate.JsonHibernateType;

@Entity
@Table(name = "TIC_TAC_TOE_TABLE")
@TypeDef(name = "gameState", typeClass = JsonHibernateType.class, defaultForType = TicTacToeState.class, parameters = { @Parameter(
        name = JsonHibernateType.CLASS_NAME_PARAMETER,
        value = "com.gogomaya.server.game.action.GameState") })
public class TicTacToeTable implements GameTable<TicTacToeState> {

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
    @JoinColumns(value = { 
            @JoinColumn(name = "SPECIFICATION_NAME", referencedColumnName = "SPECIFICATION_NAME"),
            @JoinColumn(name = "SPECIFICATION_GROUP", referencedColumnName = "SPECIFICATION_GROUP") })
    private GameSpecification specification;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "TIC_TAC_TOE_TABLE_PLAYERS", joinColumns = @JoinColumn(name = "TABLE_ID"))
    private Set<Long> players = new HashSet<Long>();

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SESSION_ID")
    private GameSession<TicTacToeState> currentSession;

    @Type(type = "gameState")
    @Column(name = "GAME_STATE", length = 4096)
    private TicTacToeState state;

    @Override
    public long getTableId() {
        return tableId;
    }

    public void setTableId(long tableId) {
        this.tableId = tableId;
    }

    @Override
    public GameServerConnection getServerResource() {
        return serverResource;
    }

    @Override
    public TicTacToeTable setServerResource(GameServerConnection serverResource) {
        this.serverResource = serverResource;
        return this;
    }

    @Override
    public Set<Long> getPlayers() {
        return players;
    }

    public void setPlayers(Set<Long> players) {
        this.players = players;
    }

    @Override
    public void addPlayer(long player) {
        this.players.add(player);
    }

    @Override
    public GameSession<TicTacToeState> getCurrentSession() {
        return currentSession;
    }

    @Override
    public void setCurrentSession(GameSession<TicTacToeState> currentSession) {
        this.currentSession = currentSession;
    }

    @Override
    public GameSpecification getSpecification() {
        return specification;
    }

    @Override
    public TicTacToeTable setSpecification(GameSpecification specification) {
        this.specification = specification;
        return this;
    }

    @Override
    public TicTacToeState getState() {
        return state;
    }

    public TicTacToeTable setState(TicTacToeState state) {
        this.state = state;
        return this;
    }

}
