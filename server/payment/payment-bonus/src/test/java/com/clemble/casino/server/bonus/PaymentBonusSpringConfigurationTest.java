package com.clemble.casino.server.bonus;

import com.clemble.casino.server.bonus.spring.PaymentBonusSpringConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
