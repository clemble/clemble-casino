package com.gogomaya.server.spring.common;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.gogomaya.server.utils.ReflectionUtils;

@Configuration
public class JsonSpringConfiguration {

    @Bean
    @Singleton
    public ObjectMapper objectMapper() {
        return createObjectMapper();
    }

    public static ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Class<?>> candidates = ReflectionUtils.findCandidates("com.gogomaya.server", JsonTypeName.class);
        List<NamedType> namedTypes = new ArrayList<NamedType>(candidates.size());
        for (Class<?> candidate : candidates) {
            namedTypes.add(new NamedType(candidate, candidate.getAnnotation(JsonTypeName.class).value()));
        }
        objectMapper.registerSubtypes(namedTypes.toArray(new NamedType[0]));
        return objectMapper;
    }

}
