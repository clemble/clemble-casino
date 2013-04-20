package com.gogomaya.server.error;

import java.io.IOException;
import java.util.Date;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;


public class GogomayaErrorFormat {

    /**
     * Custom {@link Date} Serializer, used by Jackson, through {@link JsonSerializer} annotation.
     * 
     * @author Anton Oparin
     * 
     */
    public static class GogomayaErrorSerializer extends JsonSerializer<GogomayaError> {

        @Override
        public void serialize(GogomayaError error, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException,
                JsonProcessingException {
            if (error == null)
                return;

            jsonGenerator.writeStartObject();
            jsonGenerator.writeFieldName("code");
            jsonGenerator.writeString(error.getCode());
            jsonGenerator.writeFieldName("description");
            jsonGenerator.writeString(error.getDescription());
            jsonGenerator.writeEndObject();
        }

    }

}
