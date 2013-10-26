package com.clemble.casino.game.rule.giveup;

import com.clemble.casino.game.configuration.GameRuleOptions;
import com.clemble.casino.game.rule.GameRule;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

@JsonTypeInfo(use = Id.NAME, include = As.WRAPPER_OBJECT, property = "giveUp")
@JsonTypeName("giveUp")
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
