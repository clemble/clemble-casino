package com.gogomaya.server.game.rule.construction;

import com.gogomaya.server.game.GameRuleOptions;
import com.gogomaya.server.game.rule.GameRule;

public enum PlayerNumberRule implements GameRule {

    TWO(2, 2),
    TWO_TO_SIX(2, 6);

    final private int minPlayers;
    final private int maxPlayers;

    private PlayerNumberRule(int minPlayers, int maxPlayers) {
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public int getMaxPlayres() {
        return maxPlayers;
    }

    final static public PlayerNumberRule DEFAULT = PlayerNumberRule.TWO;
    final static public GameRuleOptions<PlayerNumberRule> DEFAULT_OPTIONS = new GameRuleOptions<PlayerNumberRule>(DEFAULT);

}
