package com.gogomaya.server.player.wallet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.gogomaya.server.player.PlayerAware;

@Entity
@Table(name = "PLAYER_WALLET")
public class PlayerWallet implements PlayerAware {

    /**
     * Generated 16/02/13
     */
    private static final long serialVersionUID = 6508845694631953306L;

    @Id
    @Column(name = "PLAYER_ID")
    private long playerId;

    @Column(name = "MONEY")
    private long playerMoney;

    @Override
    public long getPlayerId() {
        return playerId;
    }

    public PlayerWallet setPlayerId(long playerId) {
        this.playerId = playerId;
        return this;
    }

    public long getPlayerMoney() {
        return playerMoney;
    }

    public PlayerWallet setPlayerMoney(long playerMoney) {
        this.playerMoney = playerMoney;
        return this;
    }

}
