package com.gogomaya.server.game;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gogomaya.server.player.PlayerAwareUtils;

public class GameAccount implements Serializable {

    private static final long serialVersionUID = -6227399075601837719L;

    private Map<Long, GamePlayerAccount> playerToAccount = new HashMap<Long, GamePlayerAccount>();

    @JsonCreator
    public GameAccount(@JsonProperty("playerStates") Collection<GamePlayerAccount> playerAccounts) {
        playerToAccount = PlayerAwareUtils.toMap(playerAccounts);
    }

    final public Collection<GamePlayerAccount> getPlayerAccounts() {
        return playerToAccount.values();
    }

    final public void setPlayerAccounts(Collection<GamePlayerAccount> playersStates) {
        this.playerToAccount = PlayerAwareUtils.toMap(playersStates);
    }

    final public GamePlayerAccount getPlayerAccount(long playerId) {
        return playerToAccount.get(playerId);
    }

    final public void setPlayerAccount(GamePlayerAccount newPlayerState) {
        playerToAccount.put(newPlayerState.getPlayerId(), newPlayerState);
    }

    final public void subMoneyLeft(long playerId, long ammount) {
        playerToAccount.get(playerId).subMoneyLeft(ammount);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((playerToAccount == null) ? 0 : playerToAccount.hashCode());
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
        GameAccount other = (GameAccount) obj;
        if (playerToAccount == null) {
            if (other.playerToAccount != null)
                return false;
        } else if (!playerToAccount.equals(other.playerToAccount))
            return false;
        return true;
    }

}
