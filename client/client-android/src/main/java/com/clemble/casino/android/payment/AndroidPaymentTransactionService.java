package com.clemble.casino.android.payment;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.List;

import org.springframework.web.client.RestTemplate;

import com.clemble.casino.ServerRegistry;
import com.clemble.casino.android.AbstractClembleCasinoOperations;
import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.payment.PlayerAccount;
import com.clemble.casino.payment.service.PaymentService;
import com.clemble.casino.web.payment.PaymentWebMapping;

public class AndroidPaymentTransactionService extends AbstractClembleCasinoOperations implements PaymentService {

    final private RestTemplate restTemplate;

    public AndroidPaymentTransactionService(RestTemplate restTemplate, ServerRegistry apiBase) {
        super(apiBase);
        this.restTemplate = checkNotNull(restTemplate);
    }

    @Override
    public PaymentTransaction getPaymentTransaction(String source, String transactionId) {
        return restTemplate
                .getForEntity(buildUriWith(PaymentWebMapping.PAYMENT_TRANSACTIONS_TRANSACTION, source, transactionId), PaymentTransaction.class)
                .getBody();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<PaymentTransaction> getPaymentTransactions(String player) {
        return restTemplate
            .getForEntity(buildUriWith(PaymentWebMapping.PAYMENT_ACCOUNTS_PLAYER_TRANSACTIONS, player), List.class)
            .getBody();
    }

    @Override
    public PlayerAccount get(String player) {
        return restTemplate
            .getForEntity(buildUriWith(PaymentWebMapping.PAYMENT_ACCOUNTS_PLAYER, player), PlayerAccount.class)
            .getBody();
    }

}
