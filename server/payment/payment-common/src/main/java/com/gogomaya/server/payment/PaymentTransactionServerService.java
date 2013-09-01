package com.gogomaya.server.payment;

import com.gogomaya.payment.PaymentTransaction;

public interface PaymentTransactionServerService {

    public PaymentTransaction process(PaymentTransaction paymentTransaction);

}
