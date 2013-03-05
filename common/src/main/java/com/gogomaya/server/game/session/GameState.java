package com.gogomaya.server.game.session;

import java.util.Set;

public class GameState {

    private Set<Long> participantIds;

    private long sequence;

    public Set<Long> getParticipantIds() {
        return participantIds;
    }

    public void setParticipantIds(Set<Long> participantIds) {
        this.participantIds = participantIds;
    }

    public long getSequence() {
        return sequence;
    }

    public void setSequence(long sequence) {
        this.sequence = sequence;
    }

}
