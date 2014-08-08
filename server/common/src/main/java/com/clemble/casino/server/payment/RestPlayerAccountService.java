package com.clemble.casino.server.payment;

import static com.clemble.casino.payment.PaymentWebMapping.*;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.List;

import com.clemble.casino.payment.PlayerAccount;
import com.clemble.casino.money.Currency;
import com.clemble.casino.payment.service.PlayerAccountServiceContract;
import com.clemble.casino.utils.CollectionUtils;
import org.springframework.web.client.RestTemplate;

public class RestPlayerAccountService implements PlayerAccountServiceContract {

    final private RestTemplate restTemplate;
    final private String base;

    public RestPlayerAccountService(String base, RestTemplate restTemplate) {
        this.restTemplate = checkNotNull(restTemplate);
        this.base = checkNotNull(base);
    }

    @Override
    public PlayerAccount getAccount(String playerWalletId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> canAfford(Collection<String> players, Currency currency, Long amount) {
        // Step 1. Generating URL
        String url = toPaymentUrl(ACCOUNTS).replace("{host}", base) + "?currency=" + currency + "&amount=" + amount;
        for(String player: players)
            url += "&player=" + player;
        // Step 2. Sending and receiving response
        return CollectionUtils.immutableList(restTemplate.getForObject(url, String[].class));
    }
}
