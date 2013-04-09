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

import com.gogomaya.server.game.bet.rule.BetRule;
import com.gogomaya.server.game.bet.rule.BetRuleFormat;
import com.gogomaya.server.game.giveup.rule.GiveUpRule;
import com.gogomaya.server.game.time.rule.TimeLimitRule;
import com.gogomaya.server.game.time.rule.TimeLimitRuleFormat;
import com.gogomaya.server.money.Currency;

@Embeddable
@TypeDefs(value = { @TypeDef(name = "betType", typeClass = BetRuleFormat.BetRuleHibernateType.class),
        @TypeDef(name = "timeType", typeClass = TimeLimitRuleFormat.TimeRuleHibernateType.class) })
public class GameRuleSpecification implements Serializable {

    /**
     * Generated 21/03/13
     */
    private static final long serialVersionUID = 4977326816846447559L;

    final public static GameRuleSpecification DEFAULT = GameRuleSpecification.create(Currency.DEFAULT, BetRule.DEFAULT, GiveUpRule.DEFAULT,
            TimeLimitRule.DEFAULT);

    @Column(name = "BET_CASH_TYPE")
    private Currency currency;
    @Type(type = "betType")
    @Columns(columns = { @Column(name = "BET_TYPE"), @Column(name = "BET_MIN_PRICE"), @Column(name = "BET_MAX_PRICE") })
    private BetRule betRule;
    @Column(name = "GIVE_UP_TYPE")
    private GiveUpRule giveUpRule;
    @Type(type = "timeType")
    @Columns(columns = { @Column(name = "TIME_TYPE"), @Column(name = "TIME_BREACH_TYPE"), @Column(name = "TIME_LIMIT") })
    private TimeLimitRule timeRule;

    public GameRuleSpecification() {
    }

    private GameRuleSpecification(@JsonProperty("cashType") final Currency cashType,
            @JsonProperty("betRule") final BetRule betRule,
            @JsonProperty("giveUpRule") final GiveUpRule giveUpRule,
            @JsonProperty("timeRule") final TimeLimitRule timeRule) {
        this.betRule = betRule == null ? BetRule.DEFAULT : betRule;
        this.giveUpRule = giveUpRule == null ? GiveUpRule.DEFAULT : giveUpRule;
        this.timeRule = timeRule == null ? TimeLimitRule.DEFAULT : timeRule;
        this.currency = cashType == null ? Currency.DEFAULT : cashType;
    }

    public Currency getCurrency() {
        return currency;
    }

    public GameRuleSpecification setCurrency(Currency cashType) {
        this.currency = cashType;
        return this;
    }

    public BetRule getBetRule() {
        return betRule;
    }

    public GameRuleSpecification setBetRule(BetRule betRule) {
        this.betRule = betRule;
        return this;
    }

    public TimeLimitRule getTimeRule() {
        return timeRule;
    }

    public GameRuleSpecification setTimeRule(TimeLimitRule timeRule) {
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
            @JsonProperty("cashType") final Currency cashType,
            @JsonProperty("betRule") final BetRule betRule,
            @JsonProperty("giveUpRule") final GiveUpRule giveUpRule,
            @JsonProperty("timeRule") final TimeLimitRule timeRule) {
        return new GameRuleSpecification(cashType, betRule, giveUpRule, timeRule);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((betRule == null) ? 0 : betRule.hashCode());
        result = prime * result + ((currency == null) ? 0 : currency.hashCode());
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
        if (currency != other.currency)
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
