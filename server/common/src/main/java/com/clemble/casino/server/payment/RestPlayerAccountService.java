package com.clemble.casino.server.payment;

import static com.clemble.casino.web.payment.PaymentWebMapping.PAYMENT_ACCOUNTS;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.List;

import com.clemble.casino.payment.PlayerAccount;
import com.clemble.casino.payment.money.Currency;
import com.clemble.casino.payment.service.PlayerAccountService;
import com.clemble.casino.utils.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.clemble.casino.ServerRegistry;
import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.payment.PaymentTransactionKey;
import com.clemble.casino.web.payment.PaymentWebMapping;

public class RestPlayerAccountService implements PlayerAccountService {

    final private RestTemplate restTemplate;
    final private String base;

    public RestPlayerAccountService(String base, RestTemplate restTemplate) {
        this.restTemplate = checkNotNull(restTemplate);
        this.base = checkNotNull(base);
    }

    @Override
    public PlayerAccount get(String playerWalletId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> canAfford(Collection<String> players, Currency currency, Long amount) {
        // Step 1. Generating URL
        String url = base + PAYMENT_ACCOUNTS + "?currency=" + currency + "&amount=" + amount;
        for(String player: players)
            url += "&player=" + player;
        // Step 2. Sending and receiving response
        return CollectionUtils.immutableList(restTemplate.getForObject(url, String[].class));
    }
}
