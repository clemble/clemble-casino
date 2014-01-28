package com.clemble.casino.server.game;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.game.specification.GameConfigurationKey;
import com.clemble.casino.game.specification.GameConfigurationKeyAware;

@NodeEntity
public class PendingGameInitiation implements GameConfigurationKeyAware, Serializable {

    /**
     * Generated 20/01/14
     */
    private static final long serialVersionUID = -5102851809614299027L;

    @GraphId
    private Long id;
    @Indexed(unique = true)
    private GameSessionKey sessionKey;
    private GameConfigurationKey configurationKey;
    @Fetch
    @RelatedTo(type = "PARTICIPATE", direction = Direction.OUTGOING)
    private Set<PendingPlayer> participants = new HashSet<>();

    public PendingGameInitiation() {
    }

    public PendingGameInitiation(GameInitiation initiation) {
        this.sessionKey = initiation.getSession();
        this.configurationKey = initiation.getConfiguration().getConfigurationKey();
        for (String player : initiation.getParticipants())
            participants.add(new PendingPlayer(player));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<PendingPlayer> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<PendingPlayer> participants) {
        this.participants = participants;
    }

    public GameSessionKey getSession() {
        return sessionKey;
    }

    public void setSession(GameSessionKey session) {
        this.sessionKey = session;
    }

    public GameConfigurationKey getConfigurationKey() {
        return configurationKey;
    }

    public void setConfiguration(GameConfigurationKey name) {
        this.configurationKey = name;
    }

    @Override
    public String toString() {
        return "penging:" + sessionKey + ":" + participants + ":" + configurationKey;
    }

}
