package com.gogomaya.server.web;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.schema.JsonSchema;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
public class GenericSchemaController {

    private ObjectMapper mapper = new ObjectMapper();
    {
        mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);
    }

    final private Map<String, Class<?>> schemaMapping = new HashMap<String, Class<?>>();

    @RequestMapping(value = "/{entity}/schema", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody()
    public ResponseEntity<?> schemaForRepository(HttpServletRequest servletRequest, @PathVariable String entity) throws IOException {
        Class<?> targetEntity = getSchemaMapping().get(entity);
        if (targetEntity == null)
            throw new IllegalArgumentException("This entity not supported");
        URI baseUri = ServletUriComponentsBuilder.fromServletMapping(servletRequest).build().toUri();

        JsonSchema schema = mapper.generateJsonSchema(targetEntity);

        URI schemaUri = UriComponentsBuilder.fromUri(baseUri).pathSegment(entity, "schema").build().toUri();
        URI requestUri = UriComponentsBuilder.fromUri(baseUri).pathSegment(entity).build().toUri();
        Resource<JsonSchema> resource = new Resource<JsonSchema>(schema, new Link(schemaUri.toString(), "self"), new Link(requestUri.toString(), entity));

        String output = mapper.writeValueAsString(resource);

        return new ResponseEntity<String>(output, HttpStatus.OK);
    }

    public Map<String, Class<?>> getSchemaMapping() {
        return schemaMapping;
    }

    public void setSchemaMapping(Map<String, String> values) throws ClassNotFoundException {
        schemaMapping.clear();
        for (Entry<String, String> entry : values.entrySet()) {
            schemaMapping.put(entry.getKey(), Class.forName(entry.getValue()));
        }
    }

    public void addSchemaMapping(String repository, Class<?> targetEntity) {
        if (repository == null || targetEntity == null)
            throw new IllegalArgumentException("Neither repository, nor class can be null");
        schemaMapping.put(repository, targetEntity);
    }

}
