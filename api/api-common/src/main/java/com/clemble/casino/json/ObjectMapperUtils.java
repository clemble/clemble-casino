package com.clemble.casino.json;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionKeyMixin;

import com.clemble.casino.utils.ReflectionUtils;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class ObjectMapperUtils {

    public static ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        // Step 1. Adding all JsonTypeName files
        List<Class<?>> candidates = ReflectionUtils.findCandidates("com.clemble.casino", JsonTypeName.class);
        List<NamedType> namedTypes = new ArrayList<NamedType>(candidates.size());
        for (Class<?> candidate : candidates) {
            namedTypes.add(new NamedType(candidate, candidate.getAnnotation(JsonTypeName.class).value()));
        }
        objectMapper.registerSubtypes(namedTypes.toArray(new NamedType[0]));
        // Step 2. Adding specific module configurations
        SimpleModule connectionKeyModule = new SimpleModule("connectionKey", new Version(0, 0, 1, "1.1.0-BUILD-SNAPSHOT", "org.springframework.social", "social-core")) {
            private static final long serialVersionUID = 2966716509403546212L;

            @Override
            public void setupModule(SetupContext context) {
                context.setMixInAnnotations(ConnectionKey.class, ConnectionKeyMixin.class);
            }

        };
        connectionKeyModule.addDeserializer(Entry.class, new CustomEntryDeserializer());
        objectMapper.registerModule(connectionKeyModule);
        // Step 3. Returning mapped ObjectMapper
        return objectMapper;
    }

}
