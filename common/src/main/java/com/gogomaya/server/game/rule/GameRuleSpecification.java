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
import com.gogomaya.server.game.rule.bet.BetRuleFormat;
import com.gogomaya.server.game.rule.bet.BetRuleFormat.CustomBetRuleType;
import com.gogomaya.server.game.rule.giveup.GiveUpRule;
import com.gogomaya.server.game.rule.giveup.GiveUpRuleFormat;
import com.gogomaya.server.game.rule.giveup.GiveUpRuleFormat.CustomGiveUpRuleType;
import com.gogomaya.server.game.rule.participant.ParticipantRule;
import com.gogomaya.server.game.rule.participant.ParticipantRuleFormat;
import com.gogomaya.server.game.rule.participant.ParticipantRuleFormat.CustomParticipantRuleType;
import com.gogomaya.server.game.rule.time.TimeRule;
import com.gogomaya.server.game.rule.time.TimeRuleFormat;
import com.gogomaya.server.game.rule.time.TimeRuleFormat.CustomTimeRuleType;
import com.gogomaya.server.player.wallet.CashType;

@Embeddable
@TypeDefs(value = { @TypeDef(name = "betRule", typeClass = CustomBetRuleType.class), @TypeDef(name = "giveUpRule", typeClass = CustomGiveUpRuleType.class),
        @TypeDef(name = "timeRule", typeClass = CustomTimeRuleType.class), @TypeDef(name = "participationRule", typeClass = CustomParticipantRuleType.class), })
public class GameRuleSpecification {

    final private static CashType DEFAULT_CASH_TYPE = CashType.FakeMoney;
    final private static BetRule DEFAULT_BET_RULE = BetRuleFormat.DEFAULT_BET_RULE;
    final private static GiveUpRule DEFAULT_GIVE_UP_RULE = GiveUpRuleFormat.DEFAULT_GIVE_UP_RULE;
    final private static TimeRule DEFAULT_TIME_RULE = TimeRuleFormat.DEFAULT_TIME_RULE;
    final private static ParticipantRule DEFAULT_PARTICIPANT_RULE = ParticipantRuleFormat.DEFAULT_PARTICIPANT_RULE;

    @Column(name = "BET_CASH_TYPE")
    @Enumerated(EnumType.STRING)
    private CashType cashType = DEFAULT_CASH_TYPE;

    @Type(type = "betRule")
    @Columns(columns = { @Column(name = "BET_TYPE"), @Column(name = "BET_MIN_PRICE"), @Column(name = "BET_MAX_PRICE") })
    private BetRule betRule = DEFAULT_BET_RULE;

    @Type(type = "giveUpRule")
    @Columns(columns = { @Column(name = "LOOSE_TYPE"), @Column(name = "LOOSE_MIN_PART") })
    private GiveUpRule giveUpRule = DEFAULT_GIVE_UP_RULE;

    @Type(type = "timeRule")
    @Columns(columns = { @Column(name = "TIME_TYPE"), @Column(name = "TIME_BREACH_TYPE"), @Column(name = "TIME_LIMIT") })
    private TimeRule timeRule = DEFAULT_TIME_RULE;

    @Type(type = "participationRule")
    @Columns(columns = { @Column(name = "PARTICIPANT_TYPE"), @Column(name = "PARTICIPANT_MATCH_TYPE"), @Column(name = "PARTICIPANT_PRIVACY_TYPE"),
            @Column(name = "PARTICIPANT_MIN"), @Column(name = "PARTICIPANT_MAX") })
    private ParticipantRule participationRule = DEFAULT_PARTICIPANT_RULE;

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

    public void setCashType(CashType cashType) {
        if (cashType != null)
            this.cashType = cashType;
    }

    public BetRule getBetRule() {
        return betRule;
    }

    public void setBetRule(BetRule betRule) {
        if (betRule == null)
            this.betRule = betRule;
    }

    public TimeRule getTimeRule() {
        return timeRule;
    }

    public void setTimeRule(TimeRule timeRule) {
        if (timeRule != null)
            this.timeRule = timeRule;
    }

    public ParticipantRule getParticipationRule() {
        return participationRule;
    }

    public void setParticipationRule(ParticipantRule participantRule) {
        if (participantRule != null)
            this.participationRule = participantRule;
    }

    public GiveUpRule getGiveUpRule() {
        return giveUpRule;
    }

    public void setGiveUpRule(GiveUpRule giveUpRule) {
        if (giveUpRule != null)
            this.giveUpRule = giveUpRule;
    }
}
