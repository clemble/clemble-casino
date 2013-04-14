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

import com.gogomaya.server.game.action.GameTable;
import com.gogomaya.server.game.connection.GameServerConnection;
import com.gogomaya.server.game.tictactoe.TicTacToeSession;
import com.gogomaya.server.game.tictactoe.TicTacToeSpecification;

@Entity
@Table(name = "TIC_TAC_TOE_TABLE")
public class TicTacToeTable implements GameTable<TicTacToeSession> {

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
            @JoinColumn(name = "SPECIFICATION_GROUP", referencedColumnName = "SPECIFICATION_GROUP")
    })
    private TicTacToeSpecification specification;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "TIC_TAC_TOE_TABLE_PLAYERS", joinColumns = @JoinColumn(name = "TABLE_ID"))
    private Set<Long> players = new HashSet<Long>();

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SESSION_ID", referencedColumnName = "TABLE_ID")
    private TicTacToeSession currentSession;

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

    public void setServerResource(GameServerConnection serverResource) {
        this.serverResource = serverResource;
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
    public TicTacToeSession getCurrentSession() {
        return currentSession;
    }

    public void setCurrentSession(TicTacToeSession currentSession) {
        this.currentSession = currentSession;
    }

    @Override
    public TicTacToeSpecification getSpecification() {
        return specification;
    }

    public void setSpecification(TicTacToeSpecification specification) {
        this.specification = specification;
    }

}
