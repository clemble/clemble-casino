package com.gogomaya.server.game;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "outcome")
@JsonSubTypes({ 
     @Type(value = PlayerWonOutcome.class, name = "playerWon"),
     @Type(value = DrawOutcome.class, name = "draw"),
     @Type(value = NoOutcome.class, name = "none")})
abstract public class GameOutcome implements Serializable {

    private static final long serialVersionUID = -7763234573172626298L;

}
