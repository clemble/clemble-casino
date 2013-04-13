package com.gogomaya.server.game.tictactoe;

import com.gogomaya.server.game.GameRuleOptions;
import com.gogomaya.server.game.GameSpecification;
import com.gogomaya.server.game.SelectRuleOptions;
import com.gogomaya.server.game.rule.bet.BetRule;
import com.gogomaya.server.game.rule.bet.FixedBetRule;
import com.gogomaya.server.game.rule.construction.MatchRule;
import com.gogomaya.server.game.rule.construction.PlayerNumberRule;
import com.gogomaya.server.game.rule.construction.PrivacyRule;
import com.gogomaya.server.game.rule.giveup.GiveUpRule;
import com.gogomaya.server.game.rule.time.MoveTimeRule;
import com.gogomaya.server.game.rule.time.TotalTimeRule;
import com.gogomaya.server.money.Currency;

public class TicTacToeOptions implements SelectRuleOptions {

    /**
     * Generated 10/04/13
     */
    private static final long serialVersionUID = -9099690454645343595L;

    final public static TicTacToeOptions DEFAULT = new TicTacToeOptions(FixedBetRule.DEFAULT_OPTIONS, Currency.DEFAULT_OPTIONS, GiveUpRule.DEFAULT_OPTIONS,
            MatchRule.DEFAULT_OPTIONS, PlayerNumberRule.DEFAULT_OPTIONS, PrivacyRule.DEFAULT_OPTIONS, MoveTimeRule.DEFAULT_OPTIONS,
            TotalTimeRule.DEFAULT_OPTIONS);

    final private GameRuleOptions<Currency> currencyOptions;

    final private GameRuleOptions<BetRule> betOptions;

    final private GameRuleOptions<GiveUpRule> giveUpOptions;

    final private GameRuleOptions<MatchRule> matchOptions;

    final private GameRuleOptions<PrivacyRule> privacyOptions;

    final private GameRuleOptions<PlayerNumberRule> numberOptions;

    final private GameRuleOptions<MoveTimeRule> moveTimeOptions;

    final private GameRuleOptions<TotalTimeRule> totalTimeOptions;

    public TicTacToeOptions(final GameRuleOptions<BetRule> betOptions,
            final GameRuleOptions<Currency> currencyOptions,
            final GameRuleOptions<GiveUpRule> giveUpOptions,
            final GameRuleOptions<MatchRule> matchOptions,
            final GameRuleOptions<PlayerNumberRule> numberOptions,
            final GameRuleOptions<PrivacyRule> privacyOptions,
            final GameRuleOptions<MoveTimeRule> moveTimeOptions,
            final GameRuleOptions<TotalTimeRule> totalTimeOptions) {
        this.betOptions = betOptions;
        this.currencyOptions = currencyOptions;
        this.giveUpOptions = giveUpOptions;
        this.matchOptions = matchOptions;
        this.numberOptions = numberOptions;
        this.privacyOptions = privacyOptions;
        this.moveTimeOptions = moveTimeOptions;
        this.totalTimeOptions = totalTimeOptions;
    }

    @Override
    public GameRuleOptions<Currency> getCurrencyOptions() {
        return currencyOptions;
    }

    @Override
    public GameRuleOptions<BetRule> getBetOptions() {
        return betOptions;
    }

    @Override
    public GameRuleOptions<GiveUpRule> getGiveUpOptions() {
        return giveUpOptions;
    }

    @Override
    public GameRuleOptions<MoveTimeRule> getMoveTimeOptions() {
        return moveTimeOptions;
    }

    @Override
    public GameRuleOptions<TotalTimeRule> getTotalTimeOptions() {
        return totalTimeOptions;
    }

    @Override
    public GameRuleOptions<MatchRule> getMatchOptions() {
        return matchOptions;
    }

    @Override
    public GameRuleOptions<PrivacyRule> getPrivacyOptions() {
        return privacyOptions;
    }

    @Override
    public GameRuleOptions<PlayerNumberRule> getNumberOptions() {
        return numberOptions;
    }

    @Override
    public boolean valid(GameSpecification specification) {
        return specification != null &&
              betOptions.contains(specification.getBetRule()) &&
              currencyOptions.contains(specification.getCurrency()) &&
              matchOptions.contains(specification.getMatchRule()) &&
              giveUpOptions.contains(specification.getGiveUpRule()) &&
              moveTimeOptions.contains(specification.getMoveTimeRule()) &&
              totalTimeOptions.contains(specification.getTotalTimeRule()) &&
              numberOptions.contains(specification.getNumberRule()) &&
              privacyOptions.contains(specification.getPrivacyRule());
    }

}
