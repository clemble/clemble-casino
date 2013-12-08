package com.clemble.casino.integration.player;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.integration.game.construction.PlayerScenarios;
import com.clemble.casino.integration.spring.IntegrationTestSpringConfiguration;
import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.player.SocialAccessGrant;
import com.clemble.casino.player.SocialConnectionData;
import com.clemble.casino.player.SocialPlayerProfile;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.facebooktestjavaapi.testuser.FacebookTestUserAccount;
import com.jayway.facebooktestjavaapi.testuser.FacebookTestUserStore;
import com.jayway.facebooktestjavaapi.testuser.impl.HttpClientFacebookTestUserStore;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { IntegrationTestSpringConfiguration.class })
public class SocialUserRegistrationITest {

    @Autowired
    public ObjectMapper objectMapper;

    @Autowired
    public PlayerScenarios playerScenarios;

    @Test
    public void testSocialDataRegistration() throws JsonParseException, JsonMappingException, IOException{
        // Step 1. Generating Facebook test user
        FacebookTestUserStore facebookStore = new HttpClientFacebookTestUserStore("262763360540886", "beb651a120e8bf7252ba4e4be4f46437");
        FacebookTestUserAccount fbAccount = facebookStore.createTestUser(true, "email");
        JsonNode fb = objectMapper.readValue(fbAccount.getUserDetails(), JsonNode.class);
        assertNotNull(fbAccount);
        assertNotNull(fb);
        // Step 2. Converting to SocialConnectionData
        SocialConnectionData connectionData = new SocialConnectionData("facebook", fb.get("id").asText(),  fbAccount.accessToken(), "", "", System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1));
        ClembleCasinoOperations casinoOperations = playerScenarios.createPlayer(connectionData);
        assertNotNull(casinoOperations);
        PlayerProfile profile = casinoOperations.profileOperations().getPlayerProfile();
        assertNotNull(profile);
        assertTrue(profile instanceof SocialPlayerProfile);
        SocialPlayerProfile socialProfile = (SocialPlayerProfile) profile;
        assertTrue(socialProfile.getSocialConnections().contains(new ConnectionKey("facebook", fb.get("id").asText())));
    }

    @Test
    public void testSocialGrantRegistration() throws JsonParseException, JsonMappingException, IOException{
        // Step 1. Generating Facebook test user
        FacebookTestUserStore facebookStore = new HttpClientFacebookTestUserStore("262763360540886", "beb651a120e8bf7252ba4e4be4f46437");
        FacebookTestUserAccount fbAccount = facebookStore.createTestUser(true, "email");
        JsonNode fb = objectMapper.readValue(fbAccount.getUserDetails(), JsonNode.class);
        assertNotNull(fbAccount);
        assertNotNull(fb);
        // Step 2. Converting to SocialConnectionData
        SocialAccessGrant accessGrant = new SocialAccessGrant("facebook", fbAccount.accessToken());
        ClembleCasinoOperations casinoOperations = playerScenarios.createPlayer(accessGrant);
        assertNotNull(casinoOperations);
        PlayerProfile profile = casinoOperations.profileOperations().getPlayerProfile();
        assertNotNull(profile);
        assertTrue(profile instanceof SocialPlayerProfile);
        SocialPlayerProfile socialProfile = (SocialPlayerProfile) profile;
        assertTrue(socialProfile.getSocialConnections().contains(new ConnectionKey("facebook", fb.get("id").asText())));
    }

}
