package com.gogomaya.server.game.table.rule;

import com.gogomaya.server.game.table.GameTableRule;

public class PlayerNumberRule implements GameTableRule {

    /**
     * Generated 21/03/13
     */
    private static final long serialVersionUID = -2230312436839159909L;

    final private int minPlayers;

    final private int maxPlayers;

    private PlayerNumberRule(int minPlayers, int maxPlayers) {
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        if (minPlayers > maxPlayers || minPlayers < 0) {
            throw new IllegalArgumentException();
        }
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public static PlayerNumberRule create(final int minPlayers, final int maxPlayers) {
        return new PlayerNumberRule(minPlayers, maxPlayers);
    }

}
