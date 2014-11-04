package com.clemble.casino.server.social;

import com.clemble.casino.server.social.spring.PlayerSocialSpringConfiguration;
import com.google.common.collect.ImmutableSet;
import org.eluder.spring.social.mongodb.MongoConnection;
import org.eluder.spring.social.mongodb.MongoConnectionTransformers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Set;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * Created by mavarazy on 11/4/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { PlayerSocialSpringConfiguration.class })
public class UserConnectionRepositoryTest {

    @Autowired
    public MongoOperations mongo;

    @Autowired
    public MongoConnectionTransformers mongoConnectionTransformers;

    @Test
    public void testCreated() {
        Set<String> providerUserIds = ImmutableSet.<String>of("A");
        Query query = query(where("providerId").is("facebook").and("providerUserId").in(providerUserIds));
        query.fields().include("userId");
    }

}
