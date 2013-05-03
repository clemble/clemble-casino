package com.gogomaya.server.game.rule.giveup;

import com.gogomaya.server.game.configuration.GameRuleOptions;
import com.gogomaya.server.game.rule.GameRule;

public enum GiveUpRule implements GameRule {

    lost(0),
    all(100),
    half(50),
    therd(33),
    quarter(25),
    tenth(10);

    final public static GiveUpRule DEFAULT = GiveUpRule.all;
    final public static GameRuleOptions<GiveUpRule> DEFAULT_OPTIONS = new GameRuleOptions<GiveUpRule>(tenth, all, half, therd, quarter, tenth);

    final public int percent;

    private GiveUpRule(int percent) {
        this.percent = percent;
    }

}
