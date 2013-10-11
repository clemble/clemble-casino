package com.clemble.casino.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
        final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
        try {
            BIRTH_DATE = DATE_FORMAT.parse("10/10/1990");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }

    final private String JSON_PRESENTATION = "{" + "\"player\":1," + "\"nickName\":\"michael.limbo\"," + "\"firstName\":\"Michael\","
            + "\"lastName\":\"Limbo\"," + "\"gender\":\"M\"," + "\"birthDate\":\"10/10/1990\"," + "\"imageUrl\":\"https://limbozo.com/\"" + "}";

    @Autowired
    public ObjectMapper objectMapper;

    @Test
    public void testSerialization() throws JsonGenerationException, JsonMappingException, IOException, ParseException {
        PlayerProfile expected = new PlayerProfile().setFirstName(FIRST_NAME).setLastName(LAST_NAME).setImageUrl(IMAGE_URL).setNickName(NICK_NAME)
                .setPlayer(USER_ID).setGender(GENDER).setBirthDate(BIRTH_DATE);
        // Step 2. Saving data to the output stream
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(arrayOutputStream, expected);
        // Step 3. Reading data from the output stream
        PlayerProfile actual = objectMapper.readValue(arrayOutputStream.toByteArray(), PlayerProfile.class);
        // Step 4. Check data value
        Assert.assertEquals(expected.getPlayer(), actual.getPlayer());
        // Assert.assertEquals(expected.getPassword(), actual.getPassword());
        Assert.assertEquals(expected.getFirstName(), actual.getFirstName());
        Assert.assertEquals(expected.getImageUrl(), actual.getImageUrl());
        Assert.assertEquals(expected.getLastName(), actual.getLastName());
        Assert.assertEquals(expected.getGender(), actual.getGender());
        Assert.assertEquals(expected.getNickName(), actual.getNickName());
        Assert.assertEquals(expected.getBirthDate(), actual.getBirthDate());
    }

    @Test
    public void testDeserialization() throws JsonGenerationException, JsonMappingException, IOException, ParseException {
        // Step 1. Reading data from the output stream
        PlayerProfile actual = objectMapper.readValue(JSON_PRESENTATION.getBytes(), PlayerProfile.class);
        // Step 4. Check data value
        Assert.assertEquals(FIRST_NAME, actual.getFirstName());
        Assert.assertEquals(IMAGE_URL, actual.getImageUrl());
        Assert.assertEquals(LAST_NAME, actual.getLastName());
        Assert.assertEquals(NICK_NAME, actual.getNickName());
        Assert.assertEquals(USER_ID, actual.getPlayer());
        Assert.assertEquals(GENDER, actual.getGender());
        Assert.assertEquals(BIRTH_DATE, actual.getBirthDate());
    }

}
