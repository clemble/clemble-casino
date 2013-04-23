package com.gogomaya.server;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gogomaya.server.player.SocialConnectionData;
import com.gogomaya.server.spring.common.CommonModuleSpringConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { CommonModuleSpringConfiguration.class })
public class SocialConnectionDataTest {

    final private String PROVIDER_ID = "facebook";
    final private String PROVIDER_USER_ID = "1232424243";
    final private String SECRET = "reedeerfest";
    final private String ACCESS_TOKEN = "23rddse2fefssdce13443";
    final private String REFRESH_TOKEN = "e22rwewfwfdscsfwerfrev";
    final private long EXPIRE_TIME = 12345678L;

    final private String JSON_PRESENTATION = "{" + 
            "\"providerId\":\"facebook\"," + 
            "\"providerUserId\":\"1232424243\"," + 
            "\"accessToken\":\"23rddse2fefssdce13443\"," +
            "\"secret\":\"reedeerfest\"," + 
            "\"refreshToken\":\"e22rwewfwfdscsfwerfrev\"," + 
            "\"expireTime\":12345678 }";

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testSerialization() throws JsonGenerationException, JsonMappingException, IOException {
        // Step 1. Generating data
        SocialConnectionData expected = new SocialConnectionData(PROVIDER_ID, PROVIDER_USER_ID, ACCESS_TOKEN, SECRET, REFRESH_TOKEN, EXPIRE_TIME);
        // Step 2. Saving data to the output stream
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(arrayOutputStream, expected);
        // Step 3. Reading data from the output stream
        SocialConnectionData actual = objectMapper.readValue(arrayOutputStream.toByteArray(), SocialConnectionData.class);
        // Step 4. Check data value
        assertEquals(expected.getAccessToken(), actual.getAccessToken());
        assertEquals(expected.getExpireTime(), actual.getExpireTime());
        assertEquals(expected.getProviderId(), actual.getProviderId());
        assertEquals(expected.getProviderUserId(), actual.getProviderUserId());
        assertEquals(expected.getSecret(), actual.getSecret());
        assertEquals(expected.getRefreshToken(), actual.getRefreshToken());

    }

    @Test
    public void testDeserialization() throws JsonParseException, JsonMappingException, IOException {
        // Step 1. Checking predefined JSON value
        SocialConnectionData expected = objectMapper.readValue(JSON_PRESENTATION.getBytes(), SocialConnectionData.class);
        // Step 2. Checking read data is t
        assertEquals(expected.getAccessToken(), ACCESS_TOKEN);
        assertEquals(expected.getExpireTime(), EXPIRE_TIME);
        assertEquals(expected.getProviderId(), PROVIDER_ID);
        assertEquals(expected.getProviderUserId(), PROVIDER_USER_ID);
        assertEquals(expected.getSecret(), SECRET);
        assertEquals(expected.getRefreshToken(), REFRESH_TOKEN);
    }
}
