package com.clemble.casino.server.payment.account;

import java.util.*;

import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.clemble.casino.money.Money;
import static com.clemble.casino.payment.PaymentWebMapping.*;

public class RestServerPlayerAccountService implements ServerPlayerAccountService {

    final private RestTemplate restTemplate;
    final private String host;

    public RestServerPlayerAccountService(String paymentServerRegistry, RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.host = paymentServerRegistry;
    }

    @Override
    public boolean canAfford(String playerId, Money amount) {
        return canAfford(Collections.singleton(playerId), amount).isEmpty();
    }

    @Override
    public List<String> canAfford(Collection<String> playerId, Money amount) {
        String url = toPaymentUrl(ACCOUNTS).replace("{host}", host)
                + "?player=" + StringUtils.collectionToCommaDelimitedString(playerId)
                + "&currency=" + amount.getCurrency()
                + "&amount=" + amount.getAmount();
        return Arrays.asList(restTemplate.getForObject(url, String[].class));
    }

}
