package com.gogomaya.server.game.table.rule;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.gogomaya.server.game.GameRuleOptions;
import com.gogomaya.server.game.rule.GameRule;

@Embeddable
public class GameTablePlayerNumberRule implements GameRule {

    /**
     * Generated 21/03/13
     */
    private static final long serialVersionUID = -2230312436839159909L;

    final static public GameTablePlayerNumberRule DEFAULT = GameTablePlayerNumberRule.create(2, 2);
    final static public GameRuleOptions<GameTablePlayerNumberRule> DEFAULT_OPTIONS = new GameRuleOptions<GameTablePlayerNumberRule>(DEFAULT);

    @Column(name = "TABLE_PLAYERS_MIN")
    private int minPlayers;

    @Column(name = "TABLE_PLAYERS_MAX")
    private int maxPlayers;

    public GameTablePlayerNumberRule() {
    }

    private GameTablePlayerNumberRule(int minPlayers, int maxPlayers) {
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        if (minPlayers > maxPlayers || minPlayers < 0) {
            throw new IllegalArgumentException();
        }
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public GameTablePlayerNumberRule setMinPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
        return this;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public GameTablePlayerNumberRule setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
        return this;
    }

    public static GameTablePlayerNumberRule create(final int minPlayers, final int maxPlayers) {
        return new GameTablePlayerNumberRule(minPlayers, maxPlayers);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + maxPlayers;
        result = prime * result + minPlayers;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GameTablePlayerNumberRule other = (GameTablePlayerNumberRule) obj;
        if (maxPlayers != other.maxPlayers)
            return false;
        if (minPlayers != other.minPlayers)
            return false;
        return true;
    }

}
