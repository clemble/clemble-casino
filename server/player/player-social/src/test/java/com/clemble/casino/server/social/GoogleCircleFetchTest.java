package com.clemble.casino.server.social;

import com.clemble.casino.server.social.adapter.GoogleSocialAdapter;
import com.clemble.casino.server.social.spring.PlayerSocialSpringConfiguration;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.clemble.casino.social.SocialProvider;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.google.api.Google;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by mavarazy on 1/2/15.
 */
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(SpringConfiguration.DEFAULT)
@ContextConfiguration(classes = { PlayerSocialSpringConfiguration.class })
public class GoogleCircleFetchTest {

    @Autowired
    public UsersConnectionRepository usersConnectionRepository;

    @Autowired
    public SocialAdapterRegistry adapterRegistry;

    @Test
    public void fetchCircle() {
        // Step 1. Fetching user A
        Set<String> keys = new HashSet<>();
        keys.add("112071524485003767564");
        String A = usersConnectionRepository.findUserIdsConnectedTo("google", keys).iterator().next();
        // Step 2. Creating connection repository
        ConnectionRepository connectionRepository = usersConnectionRepository.createConnectionRepository(A);
        // Step 3. Fetching connection
        Connection<Google> connection = connectionRepository.findConnections(Google.class).get(0);
        Google google = connection.getApi();
        // Step 4. Checking SocialAdapter can fetch connected users
        GoogleSocialAdapter socialAdapter = (GoogleSocialAdapter) adapterRegistry.getSocialAdapter(SocialProvider.google);
        // Step 5. Fetching connection keys
        Collection<ConnectionKey> connectionKeys = socialAdapter.fetchConnections(google);
        // Step 6. Checking size of connection keys
        Assert.assertNotEquals(connectionKeys.size(), 0);
    }

}
