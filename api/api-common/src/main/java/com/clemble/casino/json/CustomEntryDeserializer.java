package com.clemble.casino.json;

import java.io.IOException;
import java.util.Map.Entry;

import com.clemble.casino.ImmutablePair;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class CustomEntryDeserializer extends JsonDeserializer<Entry<String, String>>{

    @Override
    public Entry<String, String> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String key;
        String value;
        jp.nextToken();// Move to field name
        if(jp.getCurrentName().equals("key")) {
            jp.nextToken(); // Move to field value
            key = jp.getValueAsString();
            jp.nextToken(); // Move to the next field name
            jp.nextToken(); // Move to the next field value
            value = jp.getValueAsString();
        } else {
            jp.nextToken(); // Move to field value
            value = jp.getValueAsString();
            jp.nextToken(); // Move to the next field name
            jp.nextToken(); // Move to the next field value
            key = jp.getValueAsString();
        }
        jp.nextToken(); // Move to the end of the Object
        return new ImmutablePair<String, String>(key, value);
    }

}
