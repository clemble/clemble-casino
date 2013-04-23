package com.gogomaya.server.social;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gogomaya.server.spring.social.SocialModuleSpringConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SocialModuleSpringConfiguration.class)
public class InitializationTest {

    @Autowired
    //UsersConnectionRepository usersConnectionRepository;

    @Test
    public void testInitialized() {
    //    Assert.assertNotNull(usersConnectionRepository);
    }
}
