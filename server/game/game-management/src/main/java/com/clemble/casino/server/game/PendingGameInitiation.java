package com.clemble.casino.server.game;

import java.util.HashSet;
import java.util.Set;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

import com.clemble.casino.game.GamePlayerRole;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.game.specification.GameSpecification;
import com.clemble.casino.player.PlayerAwareUtils;

@NodeEntity
public class PendingGameInitiation {

    @GraphId
    private Long id;

    @Indexed(unique = true)
    private GameSessionKey session;

    // TODO Switch to SpecificationKey
    private GameSpecification specification;

    @Fetch @RelatedTo(type = "PARTICIPATE", direction = Direction.OUTGOING)
    private Set<PendingPlayer> participants = new HashSet<>();

    public PendingGameInitiation() {
    }

    public PendingGameInitiation(GameInitiation initiation) {
        this.setSession(initiation.getSession());
        this.specification = initiation.getSpecification();
        for (GamePlayerRole playerRole : initiation.getParticipants())
            participants.add(new PendingPlayer(playerRole.getPlayer()));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GameSpecification getSpecification() {
        return specification;
    }

    public void setSpecification(GameSpecification specification) {
        this.specification = specification;
    }

    public Set<PendingPlayer> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<PendingPlayer> participants) {
        this.participants = participants;
    }

    public GameSessionKey getSession() {
        return session;
    }

    public void setSession(GameSessionKey session) {
        this.session = session;
    }

    public GameInitiation toInitiation() {
        return new GameInitiation(getSession(), PlayerAwareUtils.toPlayerList(participants), specification);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((participants == null) ? 0 : participants.hashCode());
        result = prime * result + ((getSession() == null) ? 0 : getSession().hashCode());
        result = prime * result + ((specification == null) ? 0 : specification.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PendingGameInitiation other = (PendingGameInitiation) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (participants == null) {
            if (other.participants != null)
                return false;
        } else if (!participants.equals(other.participants))
            return false;
        if (getSession() == null) {
            if (other.getSession() != null)
                return false;
        } else if (!getSession().equals(other.getSession()))
            return false;
        if (specification == null) {
            if (other.specification != null)
                return false;
        } else if (!specification.equals(other.specification))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "penging:" + getSession() + ":" + participants + ":" + specification.getName();
    }

}
