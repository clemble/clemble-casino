package com.clemble.casino.server.game.construction;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import com.clemble.casino.game.GameSessionAware;
import com.clemble.casino.game.configuration.GameConfiguration;
import com.clemble.casino.game.configuration.GameConfigurationAware;
import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

import com.clemble.casino.game.construction.GameInitiation;

@NodeEntity
public class PendingGameInitiation implements GameConfigurationAware, GameSessionAware {

    /**
     * Generated 20/01/14
     */
    private static final long serialVersionUID = -5102851809614299027L;

    @GraphId
    private Long id;
    // TODO make GameSessionAware and restore configuration key serialization
    @Indexed(unique = true)
    private String sessionKey;
    private GameConfiguration configuration;
    @Fetch
    @RelatedTo(type = "PARTICIPATE", direction = Direction.OUTGOING)
    private Set<PendingPlayer> participants = new HashSet<>();

    public PendingGameInitiation() {
    }

    public PendingGameInitiation(GameInitiation initiation) {
        this.sessionKey = initiation.getSessionKey().toString();
        this.configuration = initiation.getConfiguration();
        for (String player : initiation.getParticipants())
            participants.add(new PendingPlayer(player));
    }

    public PendingGameInitiation(GameInitiation initiation, Set<PendingPlayer> participants) {
        this.sessionKey = initiation.getSessionKey().toString();
        this.configuration = initiation.getConfiguration();
        this.participants = participants;
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

    @Override
    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String session) {
        this.sessionKey = session;
    }

    @Override
    public GameConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(GameConfiguration name) {
        this.configuration = name;
    }

    public GameInitiation toInitiation(){
        LinkedHashSet<String> players = new LinkedHashSet<>();
        for(PendingPlayer pendingPlayer: participants)
            players.add(pendingPlayer.getPlayer());
        return new GameInitiation(sessionKey, players, configuration);
    }

    @Override
    public String toString() {
        return "penging:" + sessionKey + ":" + participants + ":" + configuration;
    }

}
