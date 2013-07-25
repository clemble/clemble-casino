package com.gogomaya.server.game.rule.construct;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.gogomaya.server.game.configuration.GameRuleOptions;
import com.gogomaya.server.game.rule.GameRule;

@JsonTypeName("privacy")
public enum PrivacyRule implements GameRule {

    players,
    everybody;

    final public static PrivacyRule DEFAULT = PrivacyRule.players;
    final public static GameRuleOptions<PrivacyRule> DEFAULT_OPTIONS = new GameRuleOptions<PrivacyRule>(DEFAULT);

}
