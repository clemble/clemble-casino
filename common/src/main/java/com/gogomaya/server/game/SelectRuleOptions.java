package com.gogomaya.server.game;

import com.gogomaya.server.game.rule.bet.BetRule;
import com.gogomaya.server.game.rule.construction.MatchRule;
import com.gogomaya.server.game.rule.construction.PlayerNumberRule;
import com.gogomaya.server.game.rule.construction.PrivacyRule;
import com.gogomaya.server.game.rule.giveup.GiveUpRule;
import com.gogomaya.server.game.rule.time.MoveTimeRule;
import com.gogomaya.server.game.rule.time.TotalTimeRule;
import com.gogomaya.server.money.Currency;

public interface SelectRuleOptions extends GameSpecificationOptions {

    public GameRuleOptions<Currency> getCurrencyOptions();

    public GameRuleOptions<MatchRule> getMatchOptions();

    public GameRuleOptions<PrivacyRule> getPrivacyOptions();

    public GameRuleOptions<PlayerNumberRule> getNumberOptions();

    public GameRuleOptions<? extends BetRule> getBetOptions();

    public GameRuleOptions<GiveUpRule> getGiveUpOptions();

    public GameRuleOptions<MoveTimeRule> getMoveTimeOptions();

    public GameRuleOptions<TotalTimeRule> getTotalTimeOptions();

}
