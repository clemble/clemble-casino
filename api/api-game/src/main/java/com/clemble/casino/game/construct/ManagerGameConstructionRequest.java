package com.clemble.casino.game.construct;

import java.util.Collection;
import java.util.List;

import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.GameSessionAware;
import com.clemble.casino.game.specification.GameSpecification;
import com.clemble.casino.utils.CollectionUtils;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("manager")
public class ManagerGameConstructionRequest extends ServerGameConstructionRequest implements GameSessionAware, GameOpponentsAware {

    /**
     * Generated 28/11/13
     */
    private static final long serialVersionUID = 805665880501624573L;

    final private List<String> participants;
    final private GameSessionKey sessionKey;

    @JsonCreator
    public ManagerGameConstructionRequest(@JsonProperty("participants") Collection<String> participants, @JsonProperty("session") GameSessionKey sessionKey, @JsonProperty("specification") GameSpecification specification) {
        super(specification);
        this.sessionKey = sessionKey;
        this.participants = CollectionUtils.immutableList(participants);
    }

    @Override
    public GameSessionKey getSession() {
        return sessionKey;
    }

    @Override
    public Collection<String> getParticipants() {
        return participants;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((participants == null) ? 0 : participants.hashCode());
        result = prime * result + ((sessionKey == null) ? 0 : sessionKey.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        ManagerGameConstructionRequest other = (ManagerGameConstructionRequest) obj;
        if (participants == null) {
            if (other.participants != null)
                return false;
        } else if (!participants.equals(other.participants))
            return false;
        if (sessionKey == null) {
            if (other.sessionKey != null)
                return false;
        } else if (!sessionKey.equals(other.sessionKey))
            return false;
        return true;
    }

}
