package com.gogomaya.server;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.event.ClientEvent;

public class ActionLatch implements Serializable {

    /**
     * Generated 04/07/13
     */
    private static final long serialVersionUID = -7689529505293361503L;

    final private Map<Long, ClientEvent> actions = new HashMap<Long, ClientEvent>();

    public ActionLatch(final Collection<Long> participants, final String action) {
        for (Long participant : participants) {
            actions.put(participant, new ExpectedAction(participant, action));
        }
    }

    @JsonCreator
    public ActionLatch(@JsonProperty("actions") final Collection<ClientEvent> expectedActions) {
        for (ClientEvent expectedAction : expectedActions) {
            actions.put(expectedAction.getPlayerId(), expectedAction);
        }
    }

    public boolean contains(long participant) {
        return actions.keySet().contains(participant);
    }

    public Set<Long> fetchParticipants() {
        return actions.keySet();
    }

    public Collection<ClientEvent> getActions() {
        return actions.values();
    }

    public Map<Long, ClientEvent> fetchActionsMap() {
        return actions;
    }

    public ClientEvent fetchAction(long player) {
        return actions.get(player);
    }

    public com.gogomaya.server.event.ClientEvent put(long participant, ClientEvent action) {
        if (contains(participant))
            return actions.put(participant, action);
        throw GogomayaException.fromError(GogomayaError.ServerLatchError);
    }

    public boolean complete() {
        for (ClientEvent action : actions.values())
            if (action instanceof ExpectedAction)
                return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((actions == null) ? 0 : actions.hashCode());
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
        ActionLatch other = (ActionLatch) obj;
        if (actions == null) {
            if (other.actions != null)
                return false;
        } else if (!actions.equals(other.actions))
            return false;
        return true;
    }

}
