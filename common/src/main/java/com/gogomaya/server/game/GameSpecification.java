package com.gogomaya.server.game;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "GAME_SPECIFICATION")
public class GameSpecification implements GameAware<GameSpecification> {

    /**
     * Generated 16/02/13
     */
    private static final long serialVersionUID = -243377038921039858L;

    @Id
    private long gameSpecificationId;

    @Column(name = "GAME_NAME")
    private String gameName;

    @Column(name = "MIN_PLAYERS")
    private int minPlayers;

    @Column(name = "MAX_PLAYERS")
    private int maxPlayers;

    @Column(name = "MIN_BET")
    private long minBet;

    @Column(name = "MAX_BET")
    private long maxBet;

    @Column(name = "MAX_TURN_TIME")
    private long maxTurnTime;

    @Override
    public String getGameName() {
        return gameName;
    }

    @Override
    public GameSpecification setGameName(String newGameName) {
        gameName = checkNotNull(newGameName);
        return this;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public GameSpecification setMinPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
        return this;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public GameSpecification setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
        return this;
    }

    public long getGameSpecificationId() {
        return gameSpecificationId;
    }

    public GameSpecification setGameSpecificationId(long gameSpecificationId) {
        this.gameSpecificationId = gameSpecificationId;
        return this;
    }

}
