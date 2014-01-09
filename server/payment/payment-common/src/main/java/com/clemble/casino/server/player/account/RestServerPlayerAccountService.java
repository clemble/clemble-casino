package com.clemble.casino.server.player.account;

import java.util.Collection;
import java.util.Collections;

import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.clemble.casino.ServerRegistry;
import com.clemble.casino.payment.money.Money;
import com.clemble.casino.web.payment.PaymentWebMapping;

public class RestServerPlayerAccountService implements ServerPlayerAccountService {

    final private RestTemplate restTemplate;
    final private ServerRegistry paymentServerRegistry;

    public RestServerPlayerAccountService(ServerRegistry paymentServerRegistry, RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.paymentServerRegistry = paymentServerRegistry;
    }

    @Override
    public boolean canAfford(String playerId, Money amount) {
        return canAfford(Collections.singleton(playerId), amount);
    }

    @Override
    public boolean canAfford(Collection<String> playerId, Money amount) {
        String url = paymentServerRegistry.findBase()
                + PaymentWebMapping.PAYMENT_ACCOUNTS
                + "?player=" + StringUtils.collectionToCommaDelimitedString(playerId)
                + "&currency=" + amount.getCurrency()
                + "&amount=" + amount.getAmount();
        return restTemplate.getForEntity(url, Boolean.class).getBody();
    }

}
