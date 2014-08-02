package com.clemble.casino.server.social;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;

import com.clemble.casino.server.spring.common.CommonSpringConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.clemble.casino.player.PlayerGender;
import com.clemble.casino.player.PlayerProfile;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { CommonSpringConfiguration.class })
public class PlayerProfileSerializationTest {

    final private String FIRST_NAME = "Michael";
    final private String LAST_NAME = "Limbo";
    final private String NICK_NAME = "michael.limbo";
    final private String USER_ID = "1";
    final private PlayerGender GENDER = PlayerGender.M;

    final private String JSON_PRESENTATION = "{" + "\"player\":1," + "\"nickName\":\"michael.limbo\"," + "\"firstName\":\"Michael\","
            + "\"lastName\":\"Limbo\"," + "\"gender\":\"M\"" + "}";

    @Autowired
    public ObjectMapper objectMapper;

    @Test
    public void testSerialization() throws JsonGenerationException, JsonMappingException, IOException, ParseException {
        PlayerProfile expected = new PlayerProfile()
            .setFirstName(FIRST_NAME)
            .setLastName(LAST_NAME)
            .setNickName(NICK_NAME)
            .setGender(GENDER)
            .setPlayer(USER_ID);
        // Step 2. Saving data to the output stream
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(arrayOutputStream, expected);
        // Step 3. Reading data from the output stream
        PlayerProfile actual = objectMapper.readValue(arrayOutputStream.toByteArray(), PlayerProfile.class);
        // Step 4. Check data value
        assertEquals(expected.getPlayer(), actual.getPlayer());
        // assertEquals(expected.getPassword(), actual.getPassword());
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getGender(), actual.getGender());
        assertEquals(expected.getNickName(), actual.getNickName());
        assertEquals(expected.getBirthDate(), actual.getBirthDate());
    }

    @Test
    public void testDeserialization() throws JsonGenerationException, JsonMappingException, IOException, ParseException {
        // Step 1. Reading data from the output stream
        PlayerProfile actual = objectMapper.readValue(JSON_PRESENTATION.getBytes(), PlayerProfile.class);
        // Step 4. Check data value
        assertEquals(FIRST_NAME, actual.getFirstName());
        assertEquals(LAST_NAME, actual.getLastName());
        assertEquals(NICK_NAME, actual.getNickName());
        assertEquals(USER_ID, actual.getPlayer());
        assertEquals(GENDER, actual.getGender());
    }

}
