package com.gogomaya.android.payment.service;

import static com.gogomaya.utils.Preconditions.checkNotNull;

import java.util.List;

import com.gogomaya.client.service.RestClientService;
import com.gogomaya.payment.PaymentTransaction;
import com.gogomaya.payment.service.PaymentTransactionService;
import com.gogomaya.web.payment.PaymentWebMapping;

public class AndroidPaymentTransactionService implements PaymentTransactionService {

    final private RestClientService restService;

    public AndroidPaymentTransactionService(RestClientService restService) {
        this.restService = checkNotNull(restService);
    }

    @Override
    public PaymentTransaction getPaymentTransaction(long playerId, String source, long transactionId) {
        return restService.getForEntity(PaymentWebMapping.PAYMENT_PREFIX, PaymentWebMapping.PAYMENT_TRANSACTIONS_TRANSACTION, PaymentTransaction.class, source, transactionId);
    }

    @Override
    public List<PaymentTransaction> listPlayerTransaction(long playerId) {
        return restService.getForEntityList(PaymentWebMapping.PAYMENT_PREFIX, PaymentWebMapping.PAYMENT_ACCOUNTS_PLAYER_TRANSACTIONS, PaymentTransaction.class, playerId);
    }

}
