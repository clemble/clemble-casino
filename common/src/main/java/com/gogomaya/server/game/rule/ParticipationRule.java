package com.gogomaya.server.game.rule;

import com.gogomaya.server.game.GameRule;


public class ParticipationRule extends GameRule {
    // Can additionally define player level
    // Can additionally define close game, without watchers
    // e t.c.

    final private int minParticipants;

    final private int maxParticipants;

    final private ParticipantMatchStrategy participantMatchStrategy;
    
    public ParticipationRule(final int minParticipants, final int maxParticipants, final ParticipantMatchStrategy participantMatchStrategy) {
        this.maxParticipants = maxParticipants;
        this.minParticipants = minParticipants;
        this.participantMatchStrategy = participantMatchStrategy;
    }

    public int getMinPlayers() {
        return minParticipants;
    }

    public int getMaxPlayers() {
        return maxParticipants;
    }

    public ParticipantMatchStrategy getParticipantMatchStrategy() {
        return participantMatchStrategy;
    }
}
