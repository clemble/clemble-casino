package com.gogomaya.server.game.rule;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import com.gogomaya.server.game.rule.bet.BetRule;
import com.gogomaya.server.game.rule.bet.BetRuleFormat;
import com.gogomaya.server.game.rule.giveup.GiveUpRule;
import com.gogomaya.server.game.rule.giveup.GiveUpRuleFormat;
import com.gogomaya.server.game.rule.time.TimeRule;
import com.gogomaya.server.game.rule.time.TimeRuleFormat;
import com.gogomaya.server.player.wallet.CashType;

@Embeddable
@TypeDefs(
    value = {   
        @TypeDef(name = "betType", typeClass = BetRuleFormat.BetRuleHibernateType.class),
        @TypeDef(name = "giveUpType", typeClass = GiveUpRuleFormat.GiveUpRuleHibernateType.class),
        @TypeDef(name = "timeType", typeClass = TimeRuleFormat.TimeRuleHibernateType.class)
    }
)
public class GameRuleSpecification implements Serializable {

    /**
     * Generated 21/03/13
     */
    private static final long serialVersionUID = 4977326816846447559L;

    final private static CashType DEFAULT_CASH_TYPE = CashType.FakeMoney;
    final private static BetRule DEFAULT_BET_RULE = BetRuleFormat.DEFAULT_BET_RULE;
    final private static GiveUpRule DEFAULT_GIVE_UP_RULE = GiveUpRuleFormat.DEFAULT_GIVE_UP_RULE;
    final private static TimeRule DEFAULT_TIME_RULE = TimeRuleFormat.DEFAULT_TIME_RULE;

    final public static GameRuleSpecification DEFAULT_RULE_SPECIFICATION = GameRuleSpecification.create(DEFAULT_CASH_TYPE, DEFAULT_BET_RULE,
            DEFAULT_GIVE_UP_RULE, DEFAULT_TIME_RULE);

    @Column(name = "BET_CASH_TYPE")
    private CashType cashType;
    @Type(type = "betType")
    @Columns( columns = {
            @Column(name = "BET_TYPE"),
            @Column(name = "BET_MIN_PRICE"),
            @Column(name = "BET_MAX_PRICE")
    })
    private BetRule betRule;
    @Type(type = "giveUpType")
    @Columns( columns = {
            @Column(name = "LOOSE_TYPE"),
            @Column(name = "LOOSE_MIN_PART")
    })
    private GiveUpRule giveUpRule;
    @Type(type = "timeType")
    @Columns( columns = {
            @Column(name = "TIME_TYPE"),
            @Column(name = "TIME_BREACH_TYPE"),
            @Column(name = "TIME_LIMIT") 
    })
    private TimeRule timeRule;

    public GameRuleSpecification() {
    }

    private GameRuleSpecification(@JsonProperty("cashType") final CashType cashType,
            @JsonProperty("betRule") final BetRule betRule,
            @JsonProperty("giveUpRule") final GiveUpRule giveUpRule,
            @JsonProperty("timeRule") final TimeRule timeRule) {
        this.betRule = betRule == null ? DEFAULT_BET_RULE : betRule;
        this.giveUpRule = giveUpRule == null ? DEFAULT_GIVE_UP_RULE : giveUpRule;
        this.timeRule = timeRule == null ? DEFAULT_TIME_RULE : timeRule;
        this.cashType = cashType == null ? DEFAULT_CASH_TYPE : cashType;
    }

    public CashType getCashType() {
        return cashType;
    }

    public GameRuleSpecification setCashType(CashType cashType) {
        this.cashType = cashType;
        return this;
    }

    public BetRule getBetRule() {
        return betRule;
    }

    public GameRuleSpecification setBetRule(BetRule betRule) {
        this.betRule = betRule;
        return this;
    }

    public TimeRule getTimeRule() {
        return timeRule;
    }

    public GameRuleSpecification setTimeRule(TimeRule timeRule) {
        this.timeRule = timeRule;
        return this;
    }

    public GiveUpRule getGiveUpRule() {
        return giveUpRule;
    }

    public GameRuleSpecification setGiveUpRule(GiveUpRule giveUpRule) {
        this.giveUpRule = giveUpRule;
        return this;
    }

    @JsonCreator
    public static GameRuleSpecification create(
            @JsonProperty("cashType") final CashType cashType,
            @JsonProperty("betRule") final BetRule betRule,
            @JsonProperty("giveUpRule") final GiveUpRule giveUpRule,
            @JsonProperty("timeRule") final TimeRule timeRule) {
        return new GameRuleSpecification(cashType, betRule, giveUpRule, timeRule);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((betRule == null) ? 0 : betRule.hashCode());
        result = prime * result + ((cashType == null) ? 0 : cashType.hashCode());
        result = prime * result + ((giveUpRule == null) ? 0 : giveUpRule.hashCode());
        result = prime * result + ((timeRule == null) ? 0 : timeRule.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GameRuleSpecification other = (GameRuleSpecification) obj;
        if (betRule == null) {
            if (other.betRule != null)
                return false;
        } else if (!betRule.equals(other.betRule))
            return false;
        if (cashType != other.cashType)
            return false;
        if (giveUpRule == null) {
            if (other.giveUpRule != null)
                return false;
        } else if (!giveUpRule.equals(other.giveUpRule))
            return false;
        if (timeRule == null) {
            if (other.timeRule != null)
                return false;
        } else if (!timeRule.equals(other.timeRule))
            return false;
        return true;
    }

}
