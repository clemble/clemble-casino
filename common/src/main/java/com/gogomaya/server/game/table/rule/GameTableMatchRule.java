package com.gogomaya.server.game.table.rule;

import com.gogomaya.server.game.GameRuleOptions;
import com.gogomaya.server.game.rule.GameRule;

public enum GameTableMatchRule implements GameRule {

    automatic,
    manual;

    final public static GameTableMatchRule DEFAULT = GameTableMatchRule.automatic;
    final public static GameRuleOptions<GameTableMatchRule> DEFAULT_OPTIONS = new GameRuleOptions<GameTableMatchRule>(automatic, automatic, manual);
}
