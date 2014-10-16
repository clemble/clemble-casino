package com.clemble.casino.server.payment;

import com.clemble.casino.server.payment.spring.PaymentSpringConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { PaymentSpringConfiguration.class })
public class PaymentSpringConfigurationTest {

    @Test
    public void initializationTest() {
    }

}
