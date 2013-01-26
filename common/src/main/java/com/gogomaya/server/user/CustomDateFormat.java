package com.gogomaya.server.user;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

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
    static class CustomDateDeserializer extends JsonDeserializer<Date> {
        @Override
        public Date deserialize(JsonParser jsonparser, DeserializationContext deserializationcontext) throws IOException, JsonProcessingException {
            try {
                return DATE_FORMAT.get().parse(jsonparser.getText());
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
    static class CustomDateSerializer extends JsonSerializer<Date> {

        @Override
        public void serialize(Date value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
            jgen.writeString(value != null ? DATE_FORMAT.get().format(value) : "");
        }

    }
}
