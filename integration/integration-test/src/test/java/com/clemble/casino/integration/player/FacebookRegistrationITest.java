package com.clemble.casino.integration.player;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.integration.event.EventAccumulator;
import com.clemble.casino.integration.game.construction.PlayerScenarios;
import com.clemble.casino.integration.spring.IntegrationTestSpringConfiguration;
import com.clemble.casino.player.event.PlayerDiscoveredConnectionEvent;
import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.social.SocialAccessGrant;
import com.clemble.casino.social.SocialConnectionData;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.facebooktestjavaapi.testuser.FacebookTestUserAccount;
import com.jayway.facebooktestjavaapi.testuser.FacebookTestUserStore;
import com.jayway.facebooktestjavaapi.testuser.impl.HttpClientFacebookTestUserStore;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@Ignore
@ContextConfiguration(classes = { IntegrationTestSpringConfiguration.class })
public class FacebookRegistrationITest {

    @Autowired
    public ObjectMapper objectMapper;

    @Autowired
    public PlayerScenarios playerScenarios;

    private FacebookTestUserStore facebookStore;

    @PostConstruct
    public void init(){
        try {
            facebookStore = new HttpClientFacebookTestUserStore("262763360540886", "beb651a120e8bf7252ba4e4be4f46437");
        } catch (Throwable throwable) {
            Assume.assumeTrue("Connection to google is missing", false);
        }
    }

    @Test
    public void testSocialDataRegistration() throws JsonParseException, JsonMappingException, IOException {
        // Step 1. Generating Facebook test user
        FacebookTestUserStore facebookStore = new HttpClientFacebookTestUserStore("262763360540886", "beb651a120e8bf7252ba4e4be4f46437");
        FacebookTestUserAccount fbAccount = facebookStore.createTestUser(true, "email");
        JsonNode fb = objectMapper.readValue(fbAccount.getUserDetails(), JsonNode.class);
        assertNotNull(fbAccount);
        assertNotNull(fb);
        // Step 2. Converting to SocialConnectionData
        SocialConnectionData connectionData = new SocialConnectionData("facebook", fb.get("id").asText(), fbAccount.accessToken(), "", "",
                System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1));
        ClembleCasinoOperations casinoOperations = playerScenarios.createPlayer(connectionData);
        assertNotNull(casinoOperations);
        PlayerProfile profile = casinoOperations.profileOperations().myProfile();
        assertNotNull(profile);
        assertTrue(profile instanceof PlayerProfile);
        PlayerProfile socialProfile = (PlayerProfile) profile;
        assertTrue(socialProfile.getSocialConnections().contains(new ConnectionKey("facebook", fb.get("id").asText())));
    }

    @Test
    public void testSocialDataLogin() throws JsonParseException, JsonMappingException, IOException {
        // Step 1. Generating Facebook test user
        FacebookTestUserStore facebookStore = new HttpClientFacebookTestUserStore("262763360540886", "beb651a120e8bf7252ba4e4be4f46437");
        FacebookTestUserAccount fbAccount = facebookStore.createTestUser(true, "email");
        JsonNode fb = objectMapper.readValue(fbAccount.getUserDetails(), JsonNode.class);
        assertNotNull(fbAccount);
        assertNotNull(fb);
        // Step 2. Converting to SocialConnectionData
        SocialConnectionData connectionData = new SocialConnectionData("facebook", fb.get("id").asText(), fbAccount.accessToken(), "", "",
                System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1));
        ClembleCasinoOperations A = playerScenarios.createPlayer(connectionData);
        assertNotNull(A);
        PlayerProfile profile = A.profileOperations().myProfile();
        assertNotNull(profile);
        assertTrue(profile instanceof PlayerProfile);
        PlayerProfile socialProfile = (PlayerProfile) profile;
        assertTrue(socialProfile.getSocialConnections().contains(new ConnectionKey("facebook", fb.get("id").asText())));
        // Step 3. Registering again with the same SocialAccessGrant
        ClembleCasinoOperations B = playerScenarios.createPlayer(connectionData);
        assertNotEquals(B, A);
        assertEquals(B.getPlayer(), A.getPlayer());
    }

    @Test
    public void testSocialGrantRegistration() throws JsonParseException, JsonMappingException, IOException {
        // Step 1. Generating Facebook test user
        SocialAccessGrant accessGrant = randomSocialGrant();
        // Step 2. Converting to SocialConnectionData
        ClembleCasinoOperations casinoOperations = playerScenarios.createPlayer(accessGrant);
        assertNotNull(casinoOperations);
        PlayerProfile profile = casinoOperations.profileOperations().myProfile();
        assertNotNull(profile);
        assertTrue(profile instanceof PlayerProfile);
        PlayerProfile socialProfile = (PlayerProfile) profile;
        assertTrue(socialProfile.getSocialConnections().contains(new ConnectionKey("facebook", profile.getSocialConnection("facebook").getProviderUserId())));
    }

    @Test
    public void testSocialGrantLogin() throws JsonParseException, JsonMappingException, IOException {
        SocialAccessGrant accessGrant = randomSocialGrant();
        // Step 1. Converting to SocialConnectionData
        ClembleCasinoOperations A = playerScenarios.createPlayer(accessGrant);
        assertNotNull(A);
        PlayerProfile profile = A.profileOperations().myProfile();
        assertNotNull(profile);
        assertTrue(profile instanceof PlayerProfile);
        PlayerProfile socialProfile = (PlayerProfile) profile;
        assertTrue(socialProfile.getSocialConnections().contains(new ConnectionKey("facebook", profile.getSocialConnection("facebook").getProviderUserId())));
        // Step 3. Registering again with the same SocialAccessGrant
        ClembleCasinoOperations B = playerScenarios.createPlayer(accessGrant);
        assertNotEquals(B, A);
        assertEquals(B.getPlayer(), A.getPlayer());
    }

    @Test
    public void testAutoDiscovery() throws JsonParseException, JsonMappingException, IOException, InterruptedException {
        // Step 1. Generating Facebook test user
        FacebookTestUserAccount fbAccoA = facebookStore.createTestUser(true, "email");
        SocialAccessGrant grantA = new SocialAccessGrant("facebook", fbAccoA.accessToken());
        ClembleCasinoOperations A = playerScenarios.createPlayer(grantA);
        // Step 2. Adding discovery event listener
        EventAccumulator<PlayerDiscoveredConnectionEvent> discoveryListener = new EventAccumulator<>();
        A.listenerOperations().subscribe(new EventTypeSelector(PlayerDiscoveredConnectionEvent.class), discoveryListener);
        // Step 3. Generating new user B, who is a friend of user A
        FacebookTestUserAccount fbAccoB = facebookStore.createTestUser(true, "email");
        fbAccoA.makeFriends(fbAccoB);
        fbAccoA.getFriends();
        SocialAccessGrant grantB = new SocialAccessGrant("facebook", fbAccoB.accessToken());
        playerScenarios.createPlayer(grantB);
        // Step 3. Checking connection were mapped internally
        assertNotNull(discoveryListener.waitFor(new EventTypeSelector(PlayerDiscoveredConnectionEvent.class)));
    }

    private SocialAccessGrant randomSocialGrant() throws JsonParseException, JsonMappingException, IOException{
        // Step 1. Generating Facebook test user
        FacebookTestUserAccount fbAccount = facebookStore.createTestUser(true, "email");
        assertNotNull(fbAccount);
        // Step 2. Converting to SocialConnectionData
        return new SocialAccessGrant("facebook", fbAccount.accessToken());
    }

}
