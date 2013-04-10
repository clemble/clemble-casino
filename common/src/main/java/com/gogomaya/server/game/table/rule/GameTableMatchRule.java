package com.gogomaya.server.game.table.rule;

import com.gogomaya.server.game.rule.GameRule;

public enum GameTableMatchRule implements GameRule {

    automatic,
    manual;

    final public static GameTableMatchRule DEFAULT = GameTableMatchRule.automatic;

}
