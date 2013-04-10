package com.gogomaya.server.game;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import com.gogomaya.server.game.bet.rule.BetRule;
import com.gogomaya.server.game.giveup.rule.GiveUpRule;
import com.gogomaya.server.game.table.rule.GameTableMatchRule;
import com.gogomaya.server.game.table.rule.GameTablePlayerNumberRule;
import com.gogomaya.server.game.table.rule.GameTablePrivacyRule;
import com.gogomaya.server.game.time.rule.TimeLimitRule;
import com.gogomaya.server.money.Currency;

public class GameSpecification implements Serializable {

    /**
     * Generated 27/03/13
     */
    private static final long serialVersionUID = -7713576722470320974L;

    final public static GameSpecification DEFAULT = GameSpecification.create(Currency.DEFAULT, BetRule.DEFAULT, GiveUpRule.DEFAULT, TimeLimitRule.DEFAULT,
            GameTableMatchRule.DEFAULT, GameTablePrivacyRule.DEFAULT, GameTablePlayerNumberRule.DEFAULT);

    final private Currency currency;

    final private BetRule betRule;

    final private GiveUpRule giveUpRule;

    final private TimeLimitRule timeRule;

    final private GameTableMatchRule matchRule;

    final private GameTablePrivacyRule privacyRule;

    final private GameTablePlayerNumberRule numberRule;

    private GameSpecification(Currency currency,
            BetRule betRule,
            GiveUpRule giveUpRule,
            TimeLimitRule timeLimitRule,
            GameTableMatchRule matchRule,
            GameTablePrivacyRule privacyRule,
            GameTablePlayerNumberRule numberRule) {
        this.currency = currency == null ? Currency.DEFAULT : currency;
        this.betRule = betRule == null ? BetRule.DEFAULT : betRule;
        this.giveUpRule = giveUpRule == null ? GiveUpRule.DEFAULT : giveUpRule;
        this.timeRule = timeLimitRule == null ? TimeLimitRule.DEFAULT : timeLimitRule;
        this.matchRule = matchRule == null ? GameTableMatchRule.DEFAULT : matchRule;
        this.privacyRule = privacyRule == null ? GameTablePrivacyRule.DEFAULT : privacyRule;
        this.numberRule = numberRule == null ? GameTablePlayerNumberRule.DEFAULT : numberRule;
    }

    public GameTableMatchRule getMatchRule() {
        return matchRule;
    }

    public GameTablePrivacyRule getPrivacyRule() {
        return privacyRule;
    }

    public GameTablePlayerNumberRule getNumberRule() {
        return numberRule;
    }

    public Currency getCurrency() {
        return currency;
    }

    public BetRule getBetRule() {
        return betRule;
    }

    public GiveUpRule getGiveUpRule() {
        return giveUpRule;
    }

    public TimeLimitRule getTimeRule() {
        return timeRule;
    }

    @JsonCreator
    public static GameSpecification create(
            @JsonProperty("currency") Currency currency,
            @JsonProperty("bet") BetRule betRule,
            @JsonProperty("loose") GiveUpRule giveUpRule,
            @JsonProperty("time") TimeLimitRule timeLimitRule,
            @JsonProperty("match") GameTableMatchRule matchRule,
            @JsonProperty("privacy") GameTablePrivacyRule privacyRule,
            @JsonProperty("number") GameTablePlayerNumberRule numberRule) {
        return new GameSpecification(currency, betRule, giveUpRule, timeLimitRule, matchRule, privacyRule, numberRule);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((betRule == null) ? 0 : betRule.hashCode());
        result = prime * result + ((currency == null) ? 0 : currency.hashCode());
        result = prime * result + ((giveUpRule == null) ? 0 : giveUpRule.hashCode());
        result = prime * result + ((matchRule == null) ? 0 : matchRule.hashCode());
        result = prime * result + ((numberRule == null) ? 0 : numberRule.hashCode());
        result = prime * result + ((privacyRule == null) ? 0 : privacyRule.hashCode());
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
        GameSpecification other = (GameSpecification) obj;
        if (betRule == null) {
            if (other.betRule != null)
                return false;
        } else if (!betRule.equals(other.betRule))
            return false;
        if (currency != other.currency)
            return false;
        if (giveUpRule != other.giveUpRule)
            return false;
        if (matchRule != other.matchRule)
            return false;
        if (numberRule == null) {
            if (other.numberRule != null)
                return false;
        } else if (!numberRule.equals(other.numberRule))
            return false;
        if (privacyRule != other.privacyRule)
            return false;
        if (timeRule == null) {
            if (other.timeRule != null)
                return false;
        } else if (!timeRule.equals(other.timeRule))
            return false;
        return true;
    }

}
