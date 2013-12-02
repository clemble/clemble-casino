package com.clemble.casino;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "registry_type")
@JsonSubTypes(value = {
        @Type(value = DNSBasedServerRegistry.class, name="dns")
})
public interface ServerRegistry extends Serializable {

    public String findBase();

    public String findById(String identifier);

    public String findByIdAndType(String identifier, Object type);

}
