package com.gogomaya.server.player.wallet;

import java.util.Collection;

import com.gogomaya.server.money.Money;

public interface WalletTransactionManager {

    public boolean canAfford(long playerId, Money ammount);

    public boolean canAfford(Collection<Long> playerId, Money ammount);

    public boolean canAfford(WalletOperation walletOperation);

    public void process(WalletTransaction walletTransaction);

}
