package com.gogomaya.server.player.account;

import java.util.Collection;
import java.util.Collections;

import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.gogomaya.server.money.Money;
import com.gogomaya.server.payment.web.mapping.PaymentWebMapping;
import com.gogomaya.server.player.PlayerProfile;

public class RestPlayerAccountService implements PlayerAccountService {

    final private String baseUrl;
    final private RestTemplate restTemplate;

    public RestPlayerAccountService(String baseUrl, RestTemplate restTemplate) {
        this.baseUrl = baseUrl;
        this.restTemplate = restTemplate;
    }

    @Override
    public PlayerAccount register(PlayerProfile playerProfile) {
        return restTemplate.postForEntity(baseUrl + PaymentWebMapping.ACCOUNT_PREFIX + PaymentWebMapping.PAYMENT_ACCOUNTS, HttpMethod.POST, PlayerAccount.class).getBody();
    }

    @Override
    public boolean canAfford(long playerId, Money ammount) {
        return canAfford(Collections.singleton(playerId), ammount);
    }

    @Override
    public boolean canAfford(Collection<Long> playerId, Money ammount) {
        String urlPostfix = StringUtils.collectionToDelimitedString(playerId, "&player=", "?player=", "&currency=") + ammount.getCurrency() + "&ammount=" + ammount.getAmount();
        return restTemplate.postForEntity(baseUrl + PaymentWebMapping.ACCOUNT_PREFIX + PaymentWebMapping.PAYMENT_ACCOUNTS + urlPostfix, HttpMethod.GET, Boolean.class).getBody();
    }


}
