package com.gogomaya.server.player.wallet;

import java.util.Collection;

import com.gogomaya.server.money.Money;
import com.gogomaya.server.payment.PaymentOperation;

public interface PlayerWalletService {

    public boolean canAfford(long playerId, Money ammount);

    public boolean canAfford(Collection<Long> playerId, Money ammount);

    public boolean canAfford(PaymentOperation walletOperation);

}
