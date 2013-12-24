package com.clemble.casino.server.payment;

import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.payment.service.PaymentTransactionService;
import com.clemble.casino.server.InternalService;

public interface PaymentTransactionServerService extends PaymentTransactionService, InternalService {

    public PaymentTransaction process(PaymentTransaction paymentTransaction);

}
