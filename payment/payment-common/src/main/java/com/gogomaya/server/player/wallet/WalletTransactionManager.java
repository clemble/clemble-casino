package com.gogomaya.server.player.wallet;

import com.gogomaya.server.money.Money;

public interface WalletTransactionManager {

    public void debit(long playerFrom, long playerTo, Money ammount);

}
