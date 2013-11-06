package com.clemble.casino.server;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.clemble.casino.player.NativePlayerProfile;
import com.clemble.casino.player.PlayerGender;
import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.server.spring.player.PlayerCommonSpringConfiguration;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { PlayerCommonSpringConfiguration.class })
public class PlayerProfileTest {

    final private String FIRST_NAME = "Michael";
    final private String LAST_NAME = "Limbo";
    final private String IMAGE_URL = "https://limbozo.com/";
    final private String NICK_NAME = "michael.limbo";
    final private String USER_ID = "1";
    final private PlayerGender GENDER = PlayerGender.M;
    final private Date BIRTH_DATE;

    {
        final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        try {
            BIRTH_DATE = DATE_FORMAT.parse("10/10/1990 00:00");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }

    final private String JSON_PRESENTATION = "{\"type\":\"native\"," + "\"player\":1," + "\"nickName\":\"michael.limbo\"," + "\"firstName\":\"Michael\","
            + "\"lastName\":\"Limbo\"," + "\"gender\":\"M\"," + "\"birthDate\":\"10/10/1990\"," + "\"imageUrl\":\"https://limbozo.com/\"" + "}";

    @Autowired
    public ObjectMapper objectMapper;

    @Test
    public void testSerialization() throws JsonGenerationException, JsonMappingException, IOException, ParseException {
        NativePlayerProfile expected = (NativePlayerProfile) new NativePlayerProfile().setFirstName(FIRST_NAME).setLastName(LAST_NAME).setImageUrl(IMAGE_URL).setNickName(NICK_NAME)
                .setGender(GENDER).setBirthDate(BIRTH_DATE).setPlayer(USER_ID);
        // Step 2. Saving data to the output stream
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(arrayOutputStream, expected);
        // Step 3. Reading data from the output stream
        NativePlayerProfile actual = objectMapper.readValue(arrayOutputStream.toByteArray(), NativePlayerProfile.class);
        // Step 4. Check data value
        assertEquals(expected.getPlayer(), actual.getPlayer());
        // assertEquals(expected.getPassword(), actual.getPassword());
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getImageUrl(), actual.getImageUrl());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getGender(), actual.getGender());
        assertEquals(expected.getNickName(), actual.getNickName());
        assertEquals(expected.getBirthDate(), actual.getBirthDate());
    }

    @Test
    public void testDeserialization() throws JsonGenerationException, JsonMappingException, IOException, ParseException {
        // Step 1. Reading data from the output stream
        NativePlayerProfile actual = (NativePlayerProfile) objectMapper.readValue(JSON_PRESENTATION.getBytes(), PlayerProfile.class);
        // Step 4. Check data value
        assertEquals(FIRST_NAME, actual.getFirstName());
        assertEquals(IMAGE_URL, actual.getImageUrl());
        assertEquals(LAST_NAME, actual.getLastName());
        assertEquals(NICK_NAME, actual.getNickName());
        assertEquals(USER_ID, actual.getPlayer());
        assertEquals(GENDER, actual.getGender());
        assertEquals(BIRTH_DATE, actual.getBirthDate());
    }

}
