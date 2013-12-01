package com.clemble.casino.server.payment;

import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.payment.service.PaymentTransactionService;

public interface PaymentTransactionServerService extends PaymentTransactionService{

    public PaymentTransaction process(PaymentTransaction paymentTransaction);

}
