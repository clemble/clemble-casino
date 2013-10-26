package com.clemble.casino.json;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.clemble.casino.utils.ReflectionUtils;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class ObjectMapperUtils {

    public static ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Class<?>> candidates = ReflectionUtils.findCandidates("com.clemble.casino", JsonTypeName.class);
        List<NamedType> namedTypes = new ArrayList<NamedType>(candidates.size());
        for (Class<?> candidate : candidates) {
            namedTypes.add(new NamedType(candidate, candidate.getAnnotation(JsonTypeName.class).value()));
        }
        objectMapper.registerSubtypes(namedTypes.toArray(new NamedType[0]));

        SimpleModule genericModule = new SimpleModule("generic", new Version(1, 0, 0, null, null, null));
        genericModule.addDeserializer(Entry.class, new CustomEntryDeserializer());

        objectMapper.registerModule(genericModule);
        return objectMapper;
    }

}
