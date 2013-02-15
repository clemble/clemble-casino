package com.gogomaya.server.user;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import junit.framework.Assert;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.gogomaya.server.error.GogomayaValidationService;

public class GamerProfileTest extends AbstractCommonTest {

    final private String FIRST_NAME = "Michael";
    final private String LAST_NAME = "Limbo";
    final private String IMAGE_URL = "https://limbozo.com/";
    final private String NICK_NAME = "michael.limbo";
    final private long USER_ID = 1L;
    final private Gender GENDER = Gender.M;
    final private Date BIRTH_DATE;

    {
        final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
        try {
            BIRTH_DATE = DATE_FORMAT.parse("10/10/1990");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }

    final private String JSON_PRESENTATION = "{" + "\"userId\":1," + "\"nickName\":\"michael.limbo\","
            + "\"firstName\":\"Michael\"," + "\"lastName\":\"Limbo\"," + "\"gender\":\"M\","
            + "\"birthDate\":\"10/10/1990\"," + "\"imageUrl\":\"https://limbozo.com/\"" + "}";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GogomayaValidationService validationService;

    @Test
    public void testSerialization() throws JsonGenerationException, JsonMappingException, IOException, ParseException {
        GamerProfile expected = new GamerProfile().setFirstName(FIRST_NAME).setLastName(LAST_NAME)
                .setImageUrl(IMAGE_URL).setNickName(NICK_NAME).setUserId(USER_ID).setGender(GENDER)
                .setBirthDate(BIRTH_DATE);
        // Step 2. Saving data to the output stream
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(arrayOutputStream, expected);
        // Step 3. Reading data from the output stream
        GamerProfile actual = objectMapper.readValue(arrayOutputStream.toByteArray(), GamerProfile.class);
        // Step 4. Check data value
        Assert.assertEquals(expected.getUserId(), actual.getUserId());
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
        GamerProfile actual = objectMapper.readValue(JSON_PRESENTATION.getBytes(), GamerProfile.class);
        // Step 4. Check data value
        Assert.assertEquals(FIRST_NAME, actual.getFirstName());
        Assert.assertEquals(IMAGE_URL, actual.getImageUrl());
        Assert.assertEquals(LAST_NAME, actual.getLastName());
        Assert.assertEquals(NICK_NAME, actual.getNickName());
        Assert.assertEquals(USER_ID, actual.getUserId());
        Assert.assertEquals(GENDER, actual.getGender());
        Assert.assertEquals(BIRTH_DATE, actual.getBirthDate());
    }

}
