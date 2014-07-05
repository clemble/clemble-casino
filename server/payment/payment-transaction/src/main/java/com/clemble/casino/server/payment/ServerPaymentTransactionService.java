package com.clemble.casino.server.payment;

import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.payment.service.PaymentTransactionService;
import com.clemble.casino.server.ServerService;

public interface ServerPaymentTransactionService extends PaymentTransactionService, ServerService {

    public PaymentTransaction process(PaymentTransaction paymentTransaction);

}
