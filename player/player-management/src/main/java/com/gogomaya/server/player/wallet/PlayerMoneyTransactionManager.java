package com.gogomaya.server.player.wallet;

import com.gogomaya.server.money.Money;

public interface PlayerMoneyTransactionManager {

    public void debit(long playerFrom, long playerTo, Money ammount);

}
