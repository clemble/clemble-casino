package com.gogomaya.server.game.table.rule;

import com.gogomaya.server.game.GameRuleOptions;
import com.gogomaya.server.game.rule.GameRule;

public enum GameTablePrivacyRule implements GameRule {

    all,
    players;

    final public static GameTablePrivacyRule DEFAULT = GameTablePrivacyRule.all;
    final public static GameRuleOptions<GameTablePrivacyRule> DEFAULT_OPTIONS = new GameRuleOptions<GameTablePrivacyRule>(DEFAULT);

}
