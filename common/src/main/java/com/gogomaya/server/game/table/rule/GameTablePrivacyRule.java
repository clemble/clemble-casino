package com.gogomaya.server.game.table.rule;

import com.gogomaya.server.game.table.GameTableRule;

public enum GameTablePrivacyRule implements GameTableRule {

    all,
    players;

    final public static GameTablePrivacyRule DEFAULT = GameTablePrivacyRule.all;

}
