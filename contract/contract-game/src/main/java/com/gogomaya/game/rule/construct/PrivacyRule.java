package com.gogomaya.game.rule.construct;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.gogomaya.game.configuration.GameRuleOptions;
import com.gogomaya.game.rule.GameRule;

@JsonTypeName("privacy")
public enum PrivacyRule implements GameRule {

    players,
    everybody;

    final public static PrivacyRule DEFAULT = PrivacyRule.players;
    final public static GameRuleOptions<PrivacyRule> DEFAULT_OPTIONS = new GameRuleOptions<PrivacyRule>(DEFAULT);

}
