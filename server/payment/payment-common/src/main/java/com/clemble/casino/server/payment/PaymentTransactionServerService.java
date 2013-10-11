package com.clemble.casino.server.payment;

import com.clemble.casino.payment.PaymentTransaction;

public interface PaymentTransactionServerService {

    public PaymentTransaction process(PaymentTransaction paymentTransaction);

}
