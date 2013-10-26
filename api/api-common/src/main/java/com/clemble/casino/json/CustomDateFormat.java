package com.clemble.casino.json;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;


/**
 * List of classes generic for DateFormating.
 * 
 * @author Anton Oparin
 *
 */
public class CustomDateFormat {

    final static ThreadLocal<SimpleDateFormat> DATE_FORMAT = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("dd/MM/yyyy");
        }
    };

    /**
     * Custom {@link Date} Deserializer, used by Jackson, through {@link JsonDeserializer} annotation.
     * 
     * @author Anton Oparin
     *
     */
    public static class CustomDateDeserializer extends JsonDeserializer<Date> {
        @Override
        public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            try {
                return DATE_FORMAT.get().parse(jsonParser.getText());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Custom {@link Date} Serializer, used by Jackson, through {@link JsonSerializer} annotation.
     * 
     * @author Anton Oparin
     *
     */
    public static class CustomDateSerializer extends JsonSerializer<Date> {

        @Override
        public void serialize(Date value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
            jsonGenerator.writeString(value != null ? DATE_FORMAT.get().format(value) : "");
        }

    }
}
