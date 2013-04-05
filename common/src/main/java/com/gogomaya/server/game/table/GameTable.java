package com.gogomaya.server.game.table;

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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.annotations.TypeDef;

import com.gogomaya.server.game.GameSpecification;
import com.gogomaya.server.game.GameSpecificationAware;
import com.gogomaya.server.game.GameSpecificationFormats;
import com.gogomaya.server.game.connection.GameServerConnection;
import com.gogomaya.server.game.session.GameSession;

@Entity
@Table(name = "GAME_TABLE")
@TypeDef(name = "gameSpecification", typeClass = GameSpecificationFormats.GameSpecificationHibernateType.class)
public class GameTable implements GameSpecificationAware {

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

    @Embedded
    private GameSpecification specification = GameSpecification.DEFAULT_SPECIFICATION;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "GAME_TABLE_PLAYERS", joinColumns = @JoinColumn(name = "TABLE_ID"))
    private Set<Long> players = new HashSet<Long>();

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SESSION_ID", referencedColumnName = "TABLE_ID")
    private GameSession currentSession;

    public long getTableId() {
        return tableId;
    }

    public void setTableId(long tableId) {
        this.tableId = tableId;
    }

    public GameServerConnection getServerResource() {
        return serverResource;
    }

    public void setServerResource(GameServerConnection serverResource) {
        this.serverResource = serverResource;
    }

    public Set<Long> getPlayers() {
        return players;
    }

    public void setPlayers(Set<Long> players) {
        this.players = players;
    }

    public void addPlayer(long player) {
        this.players.add(player);
    }

    public GameSession getCurrentSession() {
        return currentSession;
    }

    public void setCurrentSession(GameSession currentSession) {
        this.currentSession = currentSession;
    }

    @Override
    public GameSpecification getSpecification() {
        return specification;
    }

    public void setSpecification(GameSpecification specification) {
        this.specification = specification;
    }

}
