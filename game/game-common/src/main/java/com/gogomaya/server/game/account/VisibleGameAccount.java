package com.gogomaya.server.game.account;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.gogomaya.server.money.Money;
import com.gogomaya.server.player.PlayerAwareUtils;
import com.google.common.collect.ImmutableMap;

@JsonTypeName("visible")
public class VisibleGameAccount implements GameAccount {

    private static final long serialVersionUID = -6227399075601837719L;

    private Money bank;
    final private ImmutableMap<Long, GamePlayerAccount> playerToAccount;

    @JsonCreator
    public VisibleGameAccount(@JsonProperty("bank") Money bank, @JsonProperty("playerAccounts") Collection<GamePlayerAccount> playerAccounts) {
        this.bank = bank;
        this.playerToAccount = PlayerAwareUtils.toImmutableMap(playerAccounts);
    }

    @Override
    final public Money getBank() {
        return bank;
    }

    @Override
    final public Collection<GamePlayerAccount> getPlayerAccounts() {
        return playerToAccount.values();
    }

    @Override
    final public GamePlayerAccount getPlayerAccount(long playerId) {
        return playerToAccount.get(playerId);
    }

    @Override
    final public void subMoneyLeft(long playerId, long amount) {
        playerToAccount.get(playerId).subMoneyLeft(amount);
        bank = bank.add(amount);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((bank == null) ? 0 : bank.hashCode());
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
        VisibleGameAccount other = (VisibleGameAccount) obj;
        if (bank == null) {
            if (other.bank != null)
                return false;
        } else if (!bank.equals(other.bank))
            return false;
        if (playerToAccount == null) {
            if (other.playerToAccount != null)
                return false;
        } else if (!playerToAccount.equals(other.playerToAccount))
            return false;
        return true;
    }

}
