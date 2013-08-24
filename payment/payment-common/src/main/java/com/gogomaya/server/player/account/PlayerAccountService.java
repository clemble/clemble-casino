package com.gogomaya.server.player.account;

import java.util.Collection;

import com.gogomaya.server.money.Money;
import com.gogomaya.server.player.PlayerProfile;

public interface PlayerAccountService {

    public PlayerAccount register(PlayerProfile playerProfile);

    public boolean canAfford(long playerId, Money amount);

    public boolean canAfford(Collection<Long> playerId, Money amount);

}
