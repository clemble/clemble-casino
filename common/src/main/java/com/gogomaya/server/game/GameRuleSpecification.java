package com.gogomaya.server.game;

import static com.google.common.base.Preconditions.checkNotNull;

import com.gogomaya.server.game.rule.BetRule;
import com.gogomaya.server.game.rule.GiveUpRule;
import com.gogomaya.server.game.rule.ParticipationRule;
import com.gogomaya.server.game.rule.TimeRule;

public class GameRuleSpecification {

    final private BetRule betRule;

    final private TimeRule timeRule;

    final private ParticipationRule participationRule;

    final private GiveUpRule giveUpRule;

    public GameRuleSpecification(final BetRule betRule, final TimeRule timeRule, final ParticipationRule participationRule, final GiveUpRule giveUpRule) {
        this.betRule = checkNotNull(betRule);
        this.timeRule = checkNotNull(timeRule);
        this.participationRule = checkNotNull(participationRule);
        this.giveUpRule = giveUpRule;
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

    public GiveUpRule getGiveUpRule() {
        return giveUpRule;
    }
}
