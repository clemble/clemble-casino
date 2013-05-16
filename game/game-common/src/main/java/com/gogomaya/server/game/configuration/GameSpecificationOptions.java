package com.gogomaya.server.game.configuration;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.gogomaya.server.game.specification.GameSpecification;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ 
     @Type(value = SelectSpecificationOptions.class, name = "selectSpecification"),
     @Type(value = SelectRuleOptions.class, name = "selectRule")})
public interface GameSpecificationOptions extends Serializable {

    public boolean valid(GameSpecification specification);

}