package com.gogomaya.server.spring.web.payment;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { PaymentWebSpringConfiguration.class })
public class PaymentWebSpringConfigurationTest {

    @Test
    public void initializationTest() {
    }

}
