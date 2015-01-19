package com.clemble.casino.server.payment.listener;

import com.clemble.casino.money.Currency;
import com.clemble.casino.money.Money;
import com.clemble.casino.server.event.player.SystemPlayerCreatedEvent;
import com.clemble.casino.server.payment.repository.PlayerAccountRepository;
import com.clemble.casino.server.player.notification.SystemEventListener;

import java.util.Collections;
import com.clemble.casino.payment.PlayerAccount;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

/**
 * Created by mavarazy on 15/10/14.
 */
public class SystemPlayerAccountCreationEventListener implements SystemEventListener<SystemPlayerCreatedEvent> {

    final private PlayerAccountRepository accountRepository;

    public SystemPlayerAccountCreationEventListener(PlayerAccountRepository accountRepository) {
        this.accountRepository = checkNotNull(accountRepository);
    }

    @Override
    public void onEvent(SystemPlayerCreatedEvent event) {
        // Step 1. Creating new account
        PlayerAccount newAccount = new PlayerAccount(event.getPlayer(), Collections.<Currency, Money>emptyMap(), null);
        // Step 2. Adding new account to repository
        accountRepository.save(newAccount);
    }

    @Override
    public String getChannel() {
        return SystemPlayerCreatedEvent.CHANNEL;
    }

    @Override
    public String getQueueName() {
        return SystemPlayerCreatedEvent.CHANNEL + " > payment:construction";
    }

}

