package com.clemble.casino.server.payment.account;

import java.util.Collection;
import java.util.List;

import com.clemble.casino.payment.money.Money;
import com.clemble.casino.server.ServerService;

public interface ServerPlayerAccountService extends ServerService {

    public boolean canAfford(String player, Money amount);

    public List<String> canAfford(Collection<String> players, Money amount);

}
