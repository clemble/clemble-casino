package com.gogomaya.server.player.account;

import java.util.Collection;

import com.gogomaya.server.money.Money;
import com.gogomaya.server.payment.PaymentOperation;

public interface PlayerAccountService {

    public boolean canAfford(long playerId, Money ammount);

    public boolean canAfford(Collection<Long> playerId, Money ammount);

    public boolean canAfford(PaymentOperation paymentOperation);

}
