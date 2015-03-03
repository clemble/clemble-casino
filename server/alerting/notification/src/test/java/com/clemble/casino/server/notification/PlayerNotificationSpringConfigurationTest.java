package com.clemble.casino.server.notification;

import com.clemble.casino.game.GamePaymentSource;
import com.clemble.casino.goal.GoalPaymentSource;
import com.clemble.casino.lifecycle.management.outcome.NoOutcome;
import com.clemble.casino.money.Currency;
import com.clemble.casino.money.Money;
import com.clemble.casino.money.Operation;
import com.clemble.casino.notification.PlayerNotification;
import com.clemble.casino.payment.notification.PaymentNotification;
import com.clemble.casino.player.notification.PlayerConnectedNotification;
import com.clemble.casino.server.notification.repository.PlayerNotificationRepository;
import com.clemble.casino.server.notification.spring.PlayerNotificationSpringConfiguration;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

/**
 * Created by mavarazy on 11/29/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { PlayerNotificationSpringConfiguration.class })
public class PlayerNotificationSpringConfigurationTest {

    @Autowired
    public PlayerNotificationRepository repository;

    @Test
    public void test() {
        Assert.assertNotNull(repository);
    }

    @Test
    public void saveNotification() {
        // Step 1. Creating notification
        PaymentNotification notification = new PaymentNotification(
            "A",
            "B",
            Money.create(Currency.DEFAULT, 50),
            Operation.Credit,
            new GoalPaymentSource("goalKey", "player", "goal", new NoOutcome()),
            DateTime.now(DateTimeZone.UTC));
        PlayerConnectedNotification connectedNotification = new PlayerConnectedNotification("AB", "B", "F", DateTime.now(DateTimeZone.UTC));
        // Step 2. Save notification
        repository.save(notification);
        repository.save(connectedNotification);
        // Step 3. Checking notification
        List<PlayerNotification> notifications = repository.findByPlayerOrderByCreatedDesc(notification.getPlayer());
        Assert.assertEquals(notifications.size(), 2);
        Assert.assertTrue(notifications.contains(notification));
        Assert.assertTrue(notifications.contains(connectedNotification));
    }

}
