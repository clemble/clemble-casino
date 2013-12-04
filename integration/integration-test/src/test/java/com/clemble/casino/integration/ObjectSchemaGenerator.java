package com.clemble.casino.integration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.clemble.casino.json.ObjectMapperUtils;
import com.clemble.test.reflection.AnnotationReflectionUtils;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.factories.SchemaFactoryWrapper;

public class ObjectSchemaGenerator {

    private static ObjectMapper objectMapper;
    
    static {
        objectMapper = ObjectMapperUtils.createObjectMapper();
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
    }
    
    @Test
    public void generateSchema() throws IOException {
        Map<String, Set<Class<?>>> mappedCandidates = new HashMap<>();

        mapCandidates(AnnotationReflectionUtils.findCandidates("com.clemble.casino", JsonTypeName.class), mappedCandidates);
        mapCandidates(AnnotationReflectionUtils.findCandidates("com.clemble.casino", JsonCreator.class), mappedCandidates);

        new File("./target/generated-schema/").mkdirs();
        for (String packageName : mappedCandidates.keySet()) {
            BufferedWriter writer = new BufferedWriter(new FileWriter("./target/generated-schema/" + packageName + ".txt"));
            try {
                for (Class<?> candidate : mappedCandidates.get(packageName)) {
                    writer.write(candidate.getSimpleName());
                    writer.write("\n");
                    writer.write(generateSchema(candidate));
                    writer.write("\n");
                }
            } finally {
                try {
                    writer.close();
                } catch (IOException e) {
                }
            }
        }
    }

    private String generateSchema(Class<?> targetEntity) throws JsonProcessingException {
        SchemaFactoryWrapper visitor = new SchemaFactoryWrapper();
        objectMapper.acceptJsonFormatVisitor(objectMapper.constructType(targetEntity), visitor);
        JsonSchema schema = visitor.finalSchema();
        return objectMapper.writeValueAsString(schema);
    }

    private void mapCandidates(List<Class<?>> candidates, Map<String, Set<Class<?>>> mappedCandidates) {
        for (Class<?> candidate : AnnotationReflectionUtils.findCandidates("com.clemble.casino", JsonTypeName.class)) {
            String packageName = candidate.getPackage().getName();
            if (!mappedCandidates.containsKey(packageName)) {
                mappedCandidates.put(packageName, new HashSet<Class<?>>());
            }
            mappedCandidates.get(packageName).add(candidate);
        }
    }
}
