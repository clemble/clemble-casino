package com.clemble.casino.integration.notification;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.integration.ClembleIntegrationTest;
import com.clemble.casino.integration.game.construction.PlayerScenarios;
import com.clemble.casino.integration.utils.AsyncUtils;
import com.clemble.casino.payment.bonus.RegistrationBonusPaymentSource;
import com.clemble.casino.payment.notification.PaymentNotification;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;

/**
 * Created by mavarazy on 11/29/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ClembleIntegrationTest
public class PaymentNotificationTest {

    @Autowired
    public PlayerScenarios playerScenarios;

    @Test
    public void checkRegistrationNotification() {
        // Step 1. Create A player
        ClembleCasinoOperations A = playerScenarios.createPlayer();
        // Step 2. Check notification appeared
        AsyncUtils.check(() -> A.notificationService().myNotifications().length >= 1);
        // Step 3. Checking registration bonus
        AsyncUtils.check(() -> Arrays.asList(A.notificationService().myNotifications()).stream().anyMatch((notification) ->
                (notification instanceof PaymentNotification && ((PaymentNotification) notification).getSource() instanceof RegistrationBonusPaymentSource)
        ));
    }

}
