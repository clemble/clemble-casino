package com.clemble.casino.server.spring;

import java.net.URI;
import java.util.Arrays;

import com.clemble.casino.server.spring.common.PropertiesSpringConfiguration;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.couchbase.client.CouchbaseClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(value = PropertiesSpringConfiguration.class)
public class SecurityCouchbaseSpringConfiguration implements SpringConfiguration {

    @Bean(destroyMethod = "shutdown")
    public CouchbaseClient securityCouchbaseClient(
        @Value("${clemble.db.security.couchbase.url}") String url,
        @Value("${clemble.db.security.couchbase.bucket}") String bucket,
        @Value("${clemble.db.security.couchbase.password}") String password) throws Exception {
        return new CouchbaseClient(Arrays.asList(new URI(url)), bucket, password);
    }

}
