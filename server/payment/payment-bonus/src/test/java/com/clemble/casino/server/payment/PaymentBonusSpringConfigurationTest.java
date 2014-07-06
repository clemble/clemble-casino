package com.clemble.casino.server.payment;

import com.clemble.casino.server.spring.payment.PaymentBonusSpringConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by mavarazy on 7/5/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = PaymentBonusSpringConfiguration.class)
public class PaymentBonusSpringConfigurationTest {

    @Test
    public void initialization() {
    }

}
