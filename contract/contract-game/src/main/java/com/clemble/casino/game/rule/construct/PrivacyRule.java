package com.clemble.casino.game.rule.construct;

import com.clemble.casino.game.configuration.GameRuleOptions;
import com.clemble.casino.game.rule.GameRule;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("privacy")
public enum PrivacyRule implements GameRule {

    players,
    everybody;

    final public static PrivacyRule DEFAULT = PrivacyRule.players;
    final public static GameRuleOptions<PrivacyRule> DEFAULT_OPTIONS = new GameRuleOptions<PrivacyRule>(DEFAULT);

}
