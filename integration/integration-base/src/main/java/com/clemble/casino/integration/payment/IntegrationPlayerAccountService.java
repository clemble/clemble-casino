package com.clemble.casino.integration.payment;

import com.clemble.casino.payment.PlayerAccount;
import com.clemble.casino.payment.money.Currency;
import com.clemble.casino.payment.service.PlayerAccountService;
import com.clemble.casino.payment.service.PlayerAccountServiceContract;
import com.clemble.casino.server.payment.controller.PlayerAccountServiceController;

import java.util.Collection;
import java.util.List;

/**
 * Created by mavarazy on 8/5/14.
 */
public class IntegrationPlayerAccountService implements PlayerAccountService {

    final private String player;
    final private PlayerAccountServiceController accountService;

    public IntegrationPlayerAccountService(String player, PlayerAccountServiceController accountService) {
        this.player = player;
        this.accountService = accountService;
    }

    @Override
    public PlayerAccount myAccount() {
        return accountService.myAccount(player);
    }

    @Override
    public PlayerAccount getAccount(String playerWalletId) {
        return accountService.getAccount(playerWalletId);
    }

    @Override
    public List<String> canAfford(Collection<String> players, Currency currency, Long amount) {
        return accountService.canAfford(players, currency, amount);
    }
}
