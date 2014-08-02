package com.clemble.casino.server.bonus.spring;

import com.clemble.casino.server.spring.AbstractWebApplicationInitializer;

/**
 * Created by mavarazy on 7/4/14.
 */
public class PaymentBonusApplicationInitializer extends AbstractWebApplicationInitializer {

    public PaymentBonusApplicationInitializer(){
        super(PaymentBonusWebSpringConfiguration.class);
    }

}
