package com.gogomaya.server.game.rule;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;

import org.hibernate.annotations.Columns;

import com.gogomaya.server.game.rule.bet.BetRule;
import com.gogomaya.server.game.rule.giveup.GiveUpRule;
import com.gogomaya.server.game.rule.participant.ParticipantRule;
import com.gogomaya.server.game.rule.time.TimeRule;
import com.gogomaya.server.player.wallet.CashType;

@Embeddable
public class GameRuleSpecification {

    @Column(name = "CASH_TYPE")
    @Enumerated(EnumType.STRING)
    private CashType cashType;

//    @Columns(columns = {
//        @Column(name = "BET_TYPE"),
//        @Column(name = "MIN_PRICE"),
//        @Column(name = "MAX_PRICE")
//    })
    @Transient
    private BetRule betRule;

    @Transient
    private GiveUpRule giveUpRule;

    @Transient
    private TimeRule timeRule;

    @Transient
    private ParticipantRule participationRule;

    public GameRuleSpecification(){
    }

    public GameRuleSpecification(final CashType cashType, final BetRule betRule, final GiveUpRule giveUpRule, final TimeRule timeRule, final ParticipantRule participationRule) {
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
