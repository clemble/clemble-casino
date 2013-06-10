package com.gogomaya.server.tictactoe.event.client;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.gogomaya.server.event.ClientEvent;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
    @Type(value = TicTacToeSelectCellEvent.class, name = "select"),
    @Type(value = TicTacToeBetOnCellEvent.class, name = "bet") 
})
public interface TicTacToeEvent extends ClientEvent {

}
