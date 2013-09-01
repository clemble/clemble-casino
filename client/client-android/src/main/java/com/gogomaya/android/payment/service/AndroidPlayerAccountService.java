package com.gogomaya.android.payment.service;

import static com.gogomaya.utils.Preconditions.checkNotNull;

import com.gogomaya.client.service.RESTClientService;
import com.gogomaya.payment.PlayerAccount;
import com.gogomaya.payment.service.PlayerAccountService;
import com.gogomaya.web.payment.PaymentWebMapping;

public class AndroidPlayerAccountService implements PlayerAccountService {

    final private RESTClientService restService;
    
    public AndroidPlayerAccountService(RESTClientService restService) {
        this.restService = checkNotNull(restService);
    }

    @Override
    public PlayerAccount get(long playerId) {
        return restService.getForEntity(PaymentWebMapping.PAYMENT_PREFIX, PaymentWebMapping.PAYMENT_ACCOUNTS_PLAYER, PlayerAccount.class, playerId);
    }

}
