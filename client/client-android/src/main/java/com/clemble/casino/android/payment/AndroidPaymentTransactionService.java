package com.clemble.casino.android.payment;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.List;

import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.payment.service.PaymentTransactionService;
import com.clemble.casino.web.payment.PaymentWebMapping;
import com.clemble.casino.client.service.RestClientService;

public class AndroidPaymentTransactionService implements PaymentTransactionService {

    final private RestClientService restService;

    public AndroidPaymentTransactionService(RestClientService restService) {
        this.restService = checkNotNull(restService);
    }

    @Override
    public PaymentTransaction getPaymentTransaction(String player, String source, String transactionId) {
        return restService.getForEntity(PaymentWebMapping.PAYMENT_TRANSACTIONS_TRANSACTION, PaymentTransaction.class, source, transactionId);
    }

    @Override
    public List<PaymentTransaction> listPlayerTransaction(String player) {
        return restService.getForEntityList(PaymentWebMapping.PAYMENT_ACCOUNTS_PLAYER_TRANSACTIONS, PaymentTransaction.class, player);
    }

}
