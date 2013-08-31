package com.gogomaya.server.player.account;

import java.util.Collection;

import com.gogomaya.money.Money;
import com.gogomaya.payment.PlayerAccount;
import com.gogomaya.player.PlayerProfile;

public interface PlayerAccountProcessingService {

    public PlayerAccount register(PlayerProfile playerProfile);

    public boolean canAfford(long playerId, Money amount);

    public boolean canAfford(Collection<Long> playerId, Money amount);

}
