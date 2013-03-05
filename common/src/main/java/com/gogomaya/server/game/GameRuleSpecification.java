package com.gogomaya.server.game;

import static com.google.common.base.Preconditions.checkNotNull;

import com.gogomaya.server.game.rule.BetRule;
import com.gogomaya.server.game.rule.ParticipationRule;
import com.gogomaya.server.game.rule.TimeRule;

public class GameRuleSpecification {

    final private BetRule betRule;

    final private TimeRule timeRule;

    final private ParticipationRule participationRule;

    public GameRuleSpecification(final BetRule betRule, final TimeRule timeRule, final ParticipationRule participationRule) {
        this.betRule = checkNotNull(betRule);
        this.timeRule = checkNotNull(timeRule);
        this.participationRule = checkNotNull(participationRule);
    }

    public BetRule getBetRule() {
        return betRule;
    }

    public TimeRule getTimeRule() {
        return timeRule;
    }

    public ParticipationRule getParticipationRule() {
        return participationRule;
    }
}
