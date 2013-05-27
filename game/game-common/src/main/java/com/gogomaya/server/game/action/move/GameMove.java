package com.gogomaya.server.game.action.move;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.gogomaya.server.game.tictactoe.action.move.TicTacToeBetOnCellMove;
import com.gogomaya.server.game.tictactoe.action.move.TicTacToeSelectCellMove;
import com.gogomaya.server.player.PlayerAware;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ 
    @Type(value = TicTacToeSelectCellMove.class, name = "select"),
    @Type(value = TicTacToeBetOnCellMove.class, name = "bet"),
    @Type(value = GiveUpMove.class, name = "giveUp")})
public interface GameMove extends PlayerAware {

}
