package com.gogomaya.server.payment;

import com.gogomaya.payment.PaymentTransaction;

public interface PaymentTransactionProcessingService {

    public PaymentTransaction process(PaymentTransaction paymentTransaction);

}
