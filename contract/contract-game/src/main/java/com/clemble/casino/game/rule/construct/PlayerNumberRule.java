package com.clemble.casino.game.rule.construct;

import com.clemble.casino.game.configuration.GameRuleOptions;
import com.clemble.casino.game.rule.GameRule;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("participants")
public enum PlayerNumberRule implements GameRule {

    two(2, 2),
    twoToSix(2, 6);

    final private int minPlayers;
    final private int maxPlayers;

    private PlayerNumberRule(int minPlayers, int maxPlayers) {
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    final static public PlayerNumberRule DEFAULT = PlayerNumberRule.two;
    final static public GameRuleOptions<PlayerNumberRule> DEFAULT_OPTIONS = new GameRuleOptions<PlayerNumberRule>(DEFAULT);

}
