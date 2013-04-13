package com.gogomaya.server.game.rule.construction;

import com.gogomaya.server.game.configuration.GameRuleOptions;
import com.gogomaya.server.game.rule.GameRule;

public enum MatchRule implements GameRule {

    automatic,
    manual;

    final public static MatchRule DEFAULT = MatchRule.automatic;
    final public static GameRuleOptions<MatchRule> DEFAULT_OPTIONS = new GameRuleOptions<MatchRule>(automatic, automatic, manual);
}
