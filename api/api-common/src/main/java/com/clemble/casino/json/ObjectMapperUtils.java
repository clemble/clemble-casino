package com.clemble.casino.json;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperUtils {

    public static ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        // Step 1. Adding all JsonTypeName files
        loadJsonModule("Generic", objectMapper);
        loadJsonModule("Common", objectMapper);
        loadJsonModule("Game", objectMapper);
        loadJsonModule("GameCell", objectMapper);
        loadJsonModule("Extenstion", objectMapper);
        // Step 2. Returning mapped ObjectMapper
        return objectMapper;
    }

    private static void loadJsonModule(String module, ObjectMapper mapper) {
        try {
            ClembleJsonModule jsonModule = (ClembleJsonModule) Class.forName("com.clemble.casino.json." + module + "JsonModule").newInstance();
            mapper.registerModule(jsonModule.construct());
        } catch (Throwable throwable) {
            // Ignore
        }
    }

}
