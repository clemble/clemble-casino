package com.gogomaya.android.payment.service;

import static com.gogomaya.utils.Preconditions.checkNotNull;

import com.gogomaya.client.service.RestClientService;
import com.gogomaya.payment.PlayerAccount;
import com.gogomaya.payment.service.PlayerAccountService;
import com.gogomaya.web.payment.PaymentWebMapping;

public class AndroidPlayerAccountService implements PlayerAccountService {

    final private RestClientService restService;
    
    public AndroidPlayerAccountService(RestClientService restService) {
        this.restService = checkNotNull(restService);
    }

    @Override
    public PlayerAccount get(String playerId) {
        return restService.getForEntity(PaymentWebMapping.PAYMENT_ACCOUNTS_PLAYER, PlayerAccount.class, playerId);
    }

}
