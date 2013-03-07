package com.gogomaya.server.game;

import static com.google.common.base.Preconditions.checkNotNull;

import com.gogomaya.server.game.rule.bet.BetRule;
import com.gogomaya.server.game.rule.giveup.GiveUpRule;
import com.gogomaya.server.game.rule.participant.ParticipantRule;
import com.gogomaya.server.game.rule.time.TimeRule;

public class GameRuleSpecification {

    final private BetRule betRule;

    final private GiveUpRule giveUpRule;

    final private TimeRule timeRule;

    final private ParticipantRule participationRule;

    public GameRuleSpecification(final BetRule betRule, final GiveUpRule giveUpRule, final TimeRule timeRule, final ParticipantRule participationRule) {
        this.betRule = checkNotNull(betRule);
        this.giveUpRule = checkNotNull(giveUpRule);
        this.timeRule = checkNotNull(timeRule);
        this.participationRule = checkNotNull(participationRule);
    }

    public BetRule getBetRule() {
        return betRule;
    }

    public TimeRule getTimeRule() {
        return timeRule;
    }

    public ParticipantRule getParticipationRule() {
        return participationRule;
    }

    public GiveUpRule getGiveUpRule() {
        return giveUpRule;
    }
}
