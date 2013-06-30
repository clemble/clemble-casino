package com.gogomaya.server;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.google.common.collect.ImmutableSet;

public class ActionLatch<T> {

    final private Set<Long> participants;

    final private Map<Long, T> actions;

    public ActionLatch(final Collection<Long> participants) {
        this.participants = ImmutableSet.copyOf(participants);
        this.actions = new HashMap<Long, T>();
    }

    public boolean contains(long participant) {
        return participants.contains(participant);
    }

    public boolean get(long participant) {
        return actions.containsKey(participant);
    }

    public Set<Long> getParticipants() {
        return participants;
    }

    public Map<Long, T> get() {
        return actions;
    }

    public T put(long participant, T action) {
        if (contains(participant))
            return actions.put(participant, action);
        throw GogomayaException.fromError(GogomayaError.ServerLatchError);
    }

    public boolean complete() {
        return actions.size() == participants.size();
    }

}
