package com.gogomaya.server.game.rule;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import com.gogomaya.server.game.rule.bet.BetRule;
import com.gogomaya.server.game.rule.bet.BetRuleFormat;
import com.gogomaya.server.game.rule.giveup.GiveUpRule;
import com.gogomaya.server.game.rule.giveup.GiveUpRuleFormat;
import com.gogomaya.server.game.rule.time.TimeRule;
import com.gogomaya.server.game.rule.time.TimeRuleFormat;
import com.gogomaya.server.player.wallet.CashType;

public class GameRuleSpecification implements Serializable {

    /**
     * Generated 21/03/13
     */
    private static final long serialVersionUID = 4977326816846447559L;

    final private static CashType DEFAULT_CASH_TYPE = CashType.FakeMoney;
    final private static BetRule DEFAULT_BET_RULE = BetRuleFormat.DEFAULT_BET_RULE;
    final private static GiveUpRule DEFAULT_GIVE_UP_RULE = GiveUpRuleFormat.DEFAULT_GIVE_UP_RULE;
    final private static TimeRule DEFAULT_TIME_RULE = TimeRuleFormat.DEFAULT_TIME_RULE;

    final public static GameRuleSpecification DEFAULT_RULE_SPECIFICATION = GameRuleSpecification.create(DEFAULT_CASH_TYPE, DEFAULT_BET_RULE, DEFAULT_GIVE_UP_RULE, DEFAULT_TIME_RULE);

    final private CashType cashType;
    final private BetRule betRule;
    final private GiveUpRule giveUpRule;
    final private TimeRule timeRule;

    private GameRuleSpecification(
            @JsonProperty("cashType") final CashType cashType, 
            @JsonProperty("betRule") final BetRule betRule, 
            @JsonProperty("giveUpRule") final GiveUpRule giveUpRule,
            @JsonProperty("timeRule")final TimeRule timeRule) {
        this.betRule = betRule == null ? DEFAULT_BET_RULE : betRule;
        this.giveUpRule = giveUpRule == null ? DEFAULT_GIVE_UP_RULE : giveUpRule;
        this.timeRule = timeRule == null ? DEFAULT_TIME_RULE : timeRule;
        this.cashType = cashType == null ? DEFAULT_CASH_TYPE : cashType;
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

    public GiveUpRule getGiveUpRule() {
        return giveUpRule;
    }

    @JsonCreator
    public static GameRuleSpecification create(
            @JsonProperty("cashType") final CashType cashType, 
            @JsonProperty("betRule") final BetRule betRule, 
            @JsonProperty("giveUpRule") final GiveUpRule giveUpRule,
            @JsonProperty("timeRule")final TimeRule timeRule) {
        return new GameRuleSpecification(cashType, betRule, giveUpRule, timeRule);
    }

}
