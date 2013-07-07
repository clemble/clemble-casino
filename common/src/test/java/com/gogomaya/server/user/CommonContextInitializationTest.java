package com.gogomaya.server.user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gogomaya.server.spring.common.CommonSpringConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { CommonSpringConfiguration.class })
public class CommonContextInitializationTest {

    @Test
    public void initialized() {
    }

}
