package com.clemble.casino.android.payment.service;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import com.clemble.casino.payment.PlayerAccount;
import com.clemble.casino.payment.service.PlayerAccountService;
import com.clemble.casino.web.payment.PaymentWebMapping;
import com.clemble.casino.client.service.RestClientService;

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
