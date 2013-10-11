package com.clemble.casino.server.web;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.factories.SchemaFactoryWrapper;
import com.google.common.collect.ImmutableMap;

@Controller
public class GenericSchemaController {

    final private ObjectMapper mapper;
    final private Map<String, Class<?>> nameToClass;

    public GenericSchemaController(ObjectMapper mapper, Map<String, Class<?>> shemaToClass) {
        this.mapper = checkNotNull(mapper);
        this.nameToClass = ImmutableMap.<String, Class<?>> copyOf(shemaToClass);
    }

    @RequestMapping(value = "{entity}/schema", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody()
    public ResponseEntity<?> schemaForRepository(HttpServletRequest servletRequest, @PathVariable String entity) throws IOException {
        Class<?> targetEntity = nameToClass.get(entity);
        if (targetEntity == null)
            throw new IllegalArgumentException("This entity not supported");

        StringBuilder output = new StringBuilder();
        if (Enum.class.isAssignableFrom(targetEntity)) {
            Object[] values = targetEntity.getEnumConstants();
            output.append("{ values:[");
            for (int i = 0; i < values.length; i++)
                output.append(mapper.writeValueAsString(values[i])).append(i != values.length - 1 ? "," : "");
            output.append("]}");
        } else {

            SchemaFactoryWrapper visitor = new SchemaFactoryWrapper();
            mapper.acceptJsonFormatVisitor(mapper.constructType(targetEntity), visitor);
            JsonSchema schema = visitor.finalSchema();

            output.append(mapper.writeValueAsString(schema));
        }

        return new ResponseEntity<String>(output.toString(), HttpStatus.OK);
    }

}
