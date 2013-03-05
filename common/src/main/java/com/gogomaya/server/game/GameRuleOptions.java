package com.gogomaya.server.game;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Set;

import com.gogomaya.server.game.rule.BetRule;
import com.gogomaya.server.game.rule.ParticipationRule;
import com.gogomaya.server.game.rule.TimeRule;
import com.google.common.collect.ImmutableSet;

public class GameRuleOptions {

    final private Set<ParticipationRule> participantRules;

    final private Set<BetRule> betRules;

    final private Set<TimeRule> timeRules;
    
    public GameRuleOptions(final Set<ParticipationRule> participationRules, final Set<BetRule> betRules, final Set<TimeRule> timeRules) {
        this.participantRules = ImmutableSet.<ParticipationRule>copyOf(checkNotNull(participationRules));
        this.betRules = ImmutableSet.<BetRule>copyOf(checkNotNull(betRules));
        this.timeRules = ImmutableSet.<TimeRule>copyOf(checkNotNull(timeRules));
    }

    public Set<ParticipationRule> getParticipantRules() {
        return participantRules;
    }

    public Set<BetRule> getBetRules() {
        return betRules;
    }

    public Set<TimeRule> getTimeRules() {
        return timeRules;
    }
}
