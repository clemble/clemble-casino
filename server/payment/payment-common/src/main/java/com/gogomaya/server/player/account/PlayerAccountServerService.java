package com.gogomaya.server.player.account;

import java.util.Collection;

import com.gogomaya.money.Money;
import com.gogomaya.payment.PlayerAccount;
import com.gogomaya.player.PlayerProfile;

public interface PlayerAccountServerService {

    public PlayerAccount register(PlayerProfile playerProfile);

    public boolean canAfford(String player, Money amount);

    public boolean canAfford(Collection<String> players, Money amount);

}
