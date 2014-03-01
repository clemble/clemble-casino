package com.clemble.casino.server.player.account;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.clemble.casino.payment.PlayerAccount;
import com.clemble.casino.payment.money.Money;
import com.clemble.casino.server.repository.payment.PlayerAccountTemplate;

public class BasicServerPlayerAccountService implements ServerPlayerAccountService {

    final private PlayerAccountTemplate playerAccountRepository;

    public BasicServerPlayerAccountService(final PlayerAccountTemplate accountTemplate) {
        this.playerAccountRepository = checkNotNull(accountTemplate);
    }

    @Override
    public boolean canAfford(String player, Money amount) {
        // Step 1. Retrieving players account
        PlayerAccount playerAccount = playerAccountRepository.findOne(player);
        Money existingAmount = playerAccount.getMoney(amount.getCurrency());
        // Step 2. If existing amount is not enough player can't afford it
        return existingAmount.getAmount() >= amount.getAmount();
    }

    @Override
    public List<String> canAfford(Collection<String> players, Money amount) {
        List<String> shortOfCache = new ArrayList<>();
        for (String player : players) {
            if (!canAfford(player, amount))
                shortOfCache.add(player);
        }
        return shortOfCache;
    }

}
