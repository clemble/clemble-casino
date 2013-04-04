package com.gogomaya.server.game.table.rule;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.gogomaya.server.game.table.GameTableRule;

@Embeddable
public class PlayerNumberRule implements GameTableRule {

    /**
     * Generated 21/03/13
     */
    private static final long serialVersionUID = -2230312436839159909L;

    @Column(name = "TABLE_PLAYERS_MIN")
    private int minPlayers;

    @Column(name = "TABLE_PLAYERS_MAX")
    private int maxPlayers;

    public PlayerNumberRule() {
    }

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
    
    public PlayerNumberRule setMinPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
        return this;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }
    
    public PlayerNumberRule setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
        return this;
    }

    public static PlayerNumberRule create(final int minPlayers, final int maxPlayers) {
        return new PlayerNumberRule(minPlayers, maxPlayers);
    }

}
