package com.gogomaya.server.game;

public interface GameBoardAware<T extends GameBoardAware<T>> {

    public GameBoard getGameBoard();

    public T setGameBoard(GameBoard gameBoard);

}
