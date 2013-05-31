package com.gogomaya.server.player.wallet;

public interface WalletTransactionManager {

    public boolean canAfford(WalletOperation walletOperation);

    public void process(WalletTransaction walletTransaction);

}
