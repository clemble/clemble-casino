package com.gogomaya.server.game.rule;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import com.gogomaya.server.game.rule.bet.BetRule;
import com.gogomaya.server.game.rule.bet.BetRuleFormat.CustomBetRuleType;
import com.gogomaya.server.game.rule.giveup.GiveUpRule;
import com.gogomaya.server.game.rule.giveup.GiveUpRuleFormat.CustomGiveUpRuleType;
import com.gogomaya.server.game.rule.participant.ParticipantRule;
import com.gogomaya.server.game.rule.participant.ParticipantRuleFormat.CustomParticipantRuleType;
import com.gogomaya.server.game.rule.time.TimeRule;
import com.gogomaya.server.game.rule.time.TimeRuleFormat.CustomTimeRuleType;
import com.gogomaya.server.player.wallet.CashType;

@Embeddable
@TypeDefs(value = { @TypeDef(name = "betRule", typeClass = CustomBetRuleType.class), @TypeDef(name = "giveUpRule", typeClass = CustomGiveUpRuleType.class),
        @TypeDef(name = "timeRule", typeClass = CustomTimeRuleType.class), @TypeDef(name = "participationRule", typeClass = CustomParticipantRuleType.class), })
public class GameRuleSpecification {

    @Column(name = "BET_CASH_TYPE")
    @Enumerated(EnumType.STRING)
    private CashType cashType;

    @Columns(columns = { @Column(name = "BET_TYPE"), @Column(name = "BET_MIN_PRICE"), @Column(name = "BET_MAX_PRICE") })
    @Type(type = "betRule")
    private BetRule betRule;

    @Columns(columns = { @Column(name = "LOOSE_TYPE"), @Column(name = "LOOSE_MIN_PART") })
    @Type(type = "giveUpRule")
    private GiveUpRule giveUpRule;

    @Columns(columns = { @Column(name = "TIME_TYPE"), @Column(name = "TIME_BREACH_TYPE"), @Column(name = "TIME_LIMIT") })
    @Type(type = "timeRule")
    private TimeRule timeRule;

    @Type(type = "participationRule")
    @Columns(columns = { @Column(name = "PARTICIPANT_TYPE"), @Column(name = "PARTICIPANT_MATCH_TYPE"), @Column(name = "PARTICIPANT_PRIVACY_TYPE"),
            @Column(name = "PARTICIPANT_MIN"), @Column(name = "PARTICIPANT_MAX") })
    private ParticipantRule participationRule;

    public GameRuleSpecification() {
    }

    public GameRuleSpecification(final CashType cashType,
            final BetRule betRule,
            final GiveUpRule giveUpRule,
            final TimeRule timeRule,
            final ParticipantRule participationRule) {
        this.betRule = checkNotNull(betRule);
        this.giveUpRule = checkNotNull(giveUpRule);
        this.timeRule = checkNotNull(timeRule);
        this.participationRule = checkNotNull(participationRule);
        this.cashType = checkNotNull(cashType);
    }

    public CashType getCashType() {
        return cashType;
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
