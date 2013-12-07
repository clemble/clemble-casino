package com.clemble.casino.json;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperUtils {

    public static ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        // Step 1. Adding all JsonTypeName files
        loadJsonModule("com.clemble.casino.json.GenericJsonModule", objectMapper);
        loadJsonModule("com.clemble.casino.json.CommonJsonModule", objectMapper);
        loadJsonModule("com.clemble.casino.json.GameJsonModule", objectMapper);
        loadJsonModule("com.clemble.casino.json.GameCellJsonModule", objectMapper);
        loadJsonModule("com.clemble.casino.json.ExtenstionJsonModule", objectMapper);
        // Step 1.1. Initializing Facebook module if available
        loadJsonModule("org.springframework.social.facebook.api.impl.json.FacebookModule", objectMapper);
        // Step 2. Returning mapped ObjectMapper
        return objectMapper;
    }

    private static void loadJsonModule(String module, ObjectMapper mapper) {
        try {
            ClembleJsonModule jsonModule = (ClembleJsonModule) Class.forName(module).newInstance();
            mapper.registerModule(jsonModule.construct());
        } catch (Throwable throwable) {
            // Ignore
        }
    }

}
