package com.gogomaya.server.player.wallet;

import com.gogomaya.server.player.PlayerAware;

public class PlayerWallet implements PlayerAware<PlayerWallet> {

    /**
     * Generated 16/02/13
     */
    private static final long serialVersionUID = 6508845694631953306L;

    private long playerId;

    private long playerMoney;

    @Override
    public long getPlayerId() {
        return playerId;
    }

    @Override
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
