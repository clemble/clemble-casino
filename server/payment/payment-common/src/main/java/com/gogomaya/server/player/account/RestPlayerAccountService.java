package com.gogomaya.server.player.account;

import java.util.Collection;
import java.util.Collections;

import org.springframework.http.HttpEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.gogomaya.money.Money;
import com.gogomaya.payment.PlayerAccount;
import com.gogomaya.player.PlayerProfile;
import com.gogomaya.web.mapping.PaymentWebMapping;

public class RestPlayerAccountService implements PlayerAccountService {

    final private String baseUrl;
    final private RestTemplate restTemplate;

    public RestPlayerAccountService(String baseUrl, RestTemplate restTemplate) {
        this.baseUrl = baseUrl;
        this.restTemplate = restTemplate;
    }

    @Override
    public PlayerAccount register(PlayerProfile playerProfile) {
        HttpEntity<PlayerProfile> request = sign(playerProfile);
        return restTemplate.postForEntity(baseUrl + PaymentWebMapping.ACCOUNT_PREFIX + PaymentWebMapping.PAYMENT_ACCOUNTS, request, PlayerAccount.class)
                .getBody();
    }

    @Override
    public boolean canAfford(long playerId, Money amount) {
        return canAfford(Collections.singleton(playerId), amount);
    }

    @Override
    public boolean canAfford(Collection<Long> playerId, Money amount) {
        String urlPostfix = "?player=" + StringUtils.collectionToCommaDelimitedString(playerId) + "&currency=" + amount.getCurrency() + "&amount=" + amount.getAmount();
        return restTemplate.getForEntity(baseUrl + PaymentWebMapping.ACCOUNT_PREFIX + PaymentWebMapping.PAYMENT_ACCOUNTS + urlPostfix, Boolean.class)
                .getBody();
    }

    private <T> HttpEntity<T> sign(T value) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>(1);
        headers.add("Content-Type", "application/json");
        HttpEntity<T> request = new HttpEntity<T>(value, headers);
        return request;
    }

}
