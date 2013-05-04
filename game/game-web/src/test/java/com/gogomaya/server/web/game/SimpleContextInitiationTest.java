package com.gogomaya.server.web.game;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.gogomaya.server.spring.web.WebGameConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = WebGameConfiguration.class)
@ActiveProfiles("test")
@WebAppConfiguration
public class SimpleContextInitiationTest {

    @Test
    public void testInitialized() {
    }

}
