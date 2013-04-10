package com.gogomaya.server.game;

import java.io.Serializable;

import com.gogomaya.server.game.bet.rule.BetRule;
import com.gogomaya.server.game.giveup.rule.GiveUpRule;
import com.gogomaya.server.game.table.rule.GameTableMatchRule;
import com.gogomaya.server.game.table.rule.GameTablePlayerNumberRule;
import com.gogomaya.server.game.table.rule.GameTablePrivacyRule;
import com.gogomaya.server.game.time.rule.TimeLimitRule;
import com.gogomaya.server.money.Currency;

public class GameOptions implements Serializable {

    /**
     * Generated 10/04/13
     */
    private static final long serialVersionUID = -9099690454645343595L;

    final private GameRuleOptions<Currency> currencyOptions;

    final private GameRuleOptions<BetRule> betOptions;

    final private GameRuleOptions<GiveUpRule> giveUpOptions;

    final private GameRuleOptions<TimeLimitRule> timeOptions;

    final private GameRuleOptions<GameTableMatchRule> matchOptions;

    final private GameRuleOptions<GameTablePrivacyRule> privacyOptions;

    final private GameRuleOptions<GameTablePlayerNumberRule> numberOptions;

    public GameOptions(final GameRuleOptions<BetRule> betOptions,
            final GameRuleOptions<Currency> currencyOptions,
            final GameRuleOptions<GiveUpRule> giveUpOptions,
            final GameRuleOptions<GameTableMatchRule> matchOptions,
            final GameRuleOptions<GameTablePlayerNumberRule> numberOptions,
            final GameRuleOptions<GameTablePrivacyRule> privacyOptions,
            final GameRuleOptions<TimeLimitRule> timeOptions) {
        this.betOptions = betOptions;
        this.currencyOptions = currencyOptions;
        this.giveUpOptions = giveUpOptions;
        this.matchOptions = matchOptions;
        this.numberOptions = numberOptions;
        this.privacyOptions = privacyOptions;
        this.timeOptions = timeOptions;
    }

    public GameRuleOptions<Currency> getCurrencyOptions() {
        return currencyOptions;
    }

    public GameRuleOptions<BetRule> getBetOptions() {
        return betOptions;
    }

    public GameRuleOptions<GiveUpRule> getGiveUpOptions() {
        return giveUpOptions;
    }

    public GameRuleOptions<TimeLimitRule> getTimeOptions() {
        return timeOptions;
    }

    public GameRuleOptions<GameTableMatchRule> getMatchOptions() {
        return matchOptions;
    }

    public GameRuleOptions<GameTablePrivacyRule> getPrivacyOptions() {
        return privacyOptions;
    }

    public GameRuleOptions<GameTablePlayerNumberRule> getNumberOptions() {
        return numberOptions;
    }
}