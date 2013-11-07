package com.clemble.casino.server.spring.web;

import java.net.URI;
import java.util.Arrays;

import org.springframework.context.annotation.Bean;

import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.couchbase.client.CouchbaseClient;

public class SecurityCouchbaseSpringConfiguration implements SpringConfiguration {

    @Bean(destroyMethod = "shutdown")
    public CouchbaseClient securityCouchbaseClient() throws Exception {
        return new CouchbaseClient(Arrays.asList(new URI("http://localhost:8091/pools")), "security", "");
    }

}
