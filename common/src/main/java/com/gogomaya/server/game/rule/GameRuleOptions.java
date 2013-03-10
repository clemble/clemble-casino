package com.gogomaya.server.game.rule;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Set;

import com.gogomaya.server.game.rule.bet.BetRule;
import com.gogomaya.server.game.rule.participant.ParticipantRule;
import com.gogomaya.server.game.rule.time.TimeRule;
import com.gogomaya.server.player.wallet.CashType;
import com.google.common.collect.ImmutableSet;

public class GameRuleOptions {
    
    final private Set<CashType> cashTypes;

    final private Set<ParticipantRule> participantRules;

    final private Set<BetRule> betRules;

    final private Set<TimeRule> timeRules;
    
    public GameRuleOptions(final Set<CashType> cashTypes, final Set<ParticipantRule> participationRules, final Set<BetRule> betRules, final Set<TimeRule> timeRules) {
        this.participantRules = ImmutableSet.<ParticipantRule>copyOf(checkNotNull(participationRules));
        this.betRules = ImmutableSet.<BetRule>copyOf(checkNotNull(betRules));
        this.timeRules = ImmutableSet.<TimeRule>copyOf(checkNotNull(timeRules));
        this.cashTypes = ImmutableSet.<CashType>copyOf(cashTypes);
    }

    public Set<ParticipantRule> getParticipantRules() {
        return participantRules;
    }

    public Set<BetRule> getBetRules() {
        return betRules;
    }

    public Set<TimeRule> getTimeRules() {
        return timeRules;
    }

    public Set<CashType> getCashTypes() {
        return cashTypes;
    }
}
