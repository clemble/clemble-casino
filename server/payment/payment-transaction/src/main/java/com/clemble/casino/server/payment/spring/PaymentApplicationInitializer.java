package com.clemble.casino.server.payment.spring;

import com.clemble.casino.server.spring.AbstractWebApplicationInitializer;

public class PaymentApplicationInitializer extends AbstractWebApplicationInitializer {

    public PaymentApplicationInitializer() {
        super(PaymentSpringConfiguration.class);
    }

}
