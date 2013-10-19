package com.clemble.casino.server.user;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.couchbase.core.CouchbaseTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.clemble.casino.server.spring.common.CouchbaseSpringConfiguration;
import com.couchbase.client.CouchbaseClient;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CouchbaseSpringConfiguration.class)
public class CouchbaseSpringConfigurationTest {

    @Autowired
    public CouchbaseClient couchbaseClient;

    @Autowired
    public CouchbaseTemplate couchbaseTemplate;

    @Test
    public void initialized() {
        assertNotNull(couchbaseClient);
        assertNotNull(couchbaseTemplate);
    }

}
