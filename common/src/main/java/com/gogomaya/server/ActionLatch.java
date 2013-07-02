package com.gogomaya.server;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.event.ExpectedAction;
import com.gogomaya.server.event.SimpleExpectedAction;

public class ActionLatch {

    final private Map<Long, ExpectedAction> actions = new HashMap<Long, ExpectedAction>();

    public ActionLatch(final Collection<Long> participants, final String action) {
        for (Long participant : participants) {
            actions.put(participant, new SimpleExpectedAction(participant, action));
        }
    }

    @JsonCreator
    public ActionLatch(@JsonProperty("actions") final Collection<ExpectedAction> expectedActions) {
        for (ExpectedAction expectedAction : expectedActions) {
            actions.put(expectedAction.getPlayerId(), expectedAction);
        }
    }

    public boolean contains(long participant) {
        return actions.keySet().contains(participant);
    }

    public boolean get(long participant) {
        return actions.containsKey(participant);
    }

    public Set<Long> fetchParticipants() {
        return actions.keySet();
    }

    public Collection<ExpectedAction> getActions() {
        return actions.values();
    }

    public Map<Long, ExpectedAction> fetchActionsMap() {
        return actions;
    }

    public ExpectedAction put(long participant, ExpectedAction action) {
        if (contains(participant))
            return actions.put(participant, action);
        throw GogomayaException.fromError(GogomayaError.ServerLatchError);
    }

    public boolean complete() {
        return actions.size() == actions.keySet().size();
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
