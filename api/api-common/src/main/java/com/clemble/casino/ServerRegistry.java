package com.clemble.casino;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "registry_type")
public interface ServerRegistry extends Serializable {

    public String findBase();

    public String findById(String identifier);

    public String findByIdAndType(String identifier, Object type);

}
