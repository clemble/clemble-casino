package com.clemble.casino.server.post;

import com.clemble.casino.server.post.spring.PlayerFeedSpringConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by mavarazy on 11/30/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = PlayerFeedSpringConfiguration.class)
public class PlayerPostSpringConfigurationTest {

    @Test
    public void test() {
    }

}
