package com.gogomaya.json;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.gogomaya.utils.ReflectionUtils;

public class ObjectMapperUtils {

    public static ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Class<?>> candidates = ReflectionUtils.findCandidates("com.gogomaya", JsonTypeName.class);
        List<NamedType> namedTypes = new ArrayList<NamedType>(candidates.size());
        for (Class<?> candidate : candidates) {
            namedTypes.add(new NamedType(candidate, candidate.getAnnotation(JsonTypeName.class).value()));
        }
        objectMapper.registerSubtypes(namedTypes.toArray(new NamedType[0]));
        return objectMapper;
    }

}
