package com.clemble.casino.server.user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.clemble.casino.server.spring.common.CommonSpringConfiguration;
import com.couchbase.client.CouchbaseClient;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CommonSpringConfiguration.class)
public class CommonSpringConfigurationTest {

    @Autowired
    private CouchbaseClient couchbaseClient;

    @Test
    public void initialized() {
    }

}
