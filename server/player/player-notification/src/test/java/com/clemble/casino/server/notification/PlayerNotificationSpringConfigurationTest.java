package com.clemble.casino.server.notification;

import com.clemble.casino.server.notification.repository.PlayerNotificationRepository;
import com.clemble.casino.server.notification.spring.PlayerNotificationSpringConfiguration;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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

}
