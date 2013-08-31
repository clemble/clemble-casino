package com.gogomaya.android.payment.service;

import static com.gogomaya.utils.Preconditions.checkNotNull;

import com.gogomaya.payment.PlayerAccount;
import com.gogomaya.payment.service.PlayerAccountService;
import com.gogomaya.player.service.RESTService;
import com.gogomaya.web.payment.PaymentWebMapping;

public class AndroidPlayerAccountService implements PlayerAccountService {

    final private RESTService restService;
    
    public AndroidPlayerAccountService(RESTService restService) {
        this.restService = checkNotNull(restService);
    }

    @Override
    public PlayerAccount get(long playerId) {
        return restService.getForEntity(PaymentWebMapping.PAYMENT_PREFIX, PaymentWebMapping.PAYMENT_ACCOUNTS_PLAYER, PlayerAccount.class, playerId);
    }

}
