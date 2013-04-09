package com.gogomaya.server.game.table.rule;

import com.gogomaya.server.game.table.GameTableRule;

public enum GameTableMatchRule implements GameTableRule {

    automatic,
    manual;

    final public static GameTableMatchRule DEFAULT = GameTableMatchRule.automatic;

}
