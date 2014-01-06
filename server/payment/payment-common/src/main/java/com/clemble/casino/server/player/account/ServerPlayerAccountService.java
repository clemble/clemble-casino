package com.clemble.casino.server.player.account;

import java.util.Collection;

import com.clemble.casino.payment.PlayerAccount;
import com.clemble.casino.payment.money.Money;
import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.server.ServerService;

public interface ServerPlayerAccountService extends ServerService {

    public PlayerAccount register(PlayerProfile playerProfile);

    public boolean canAfford(String player, Money amount);

    public boolean canAfford(Collection<String> players, Money amount);

}