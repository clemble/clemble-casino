package com.gogomaya.server.game.rule.construction;

import com.gogomaya.server.game.GameRuleOptions;
import com.gogomaya.server.game.rule.GameRule;

public enum PrivacyRule implements GameRule {

    players,
    everybody;

    final public static PrivacyRule DEFAULT = PrivacyRule.players;
    final public static GameRuleOptions<PrivacyRule> DEFAULT_OPTIONS = new GameRuleOptions<PrivacyRule>(DEFAULT);

}
