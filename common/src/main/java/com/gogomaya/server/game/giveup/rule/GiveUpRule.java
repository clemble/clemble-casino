package com.gogomaya.server.game.giveup.rule;

import com.gogomaya.server.game.rule.GameRule;

public enum GiveUpRule implements GameRule {
    lost,
    all,
    half,
    therd,
    quarter,
    tenth;

    final public static GiveUpRule DEFAULT = GiveUpRule.all;
}
