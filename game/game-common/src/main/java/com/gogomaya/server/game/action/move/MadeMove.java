package com.gogomaya.server.game.action.move;

public class MadeMove {

    private int moveId;

    private GameMove move;

    private long creationTime;

    public int getMoveId() {
        return moveId;
    }

    public void setMoveId(int moveId) {
        this.moveId = moveId;
    }

    public GameMove getMove() {
        return move;
    }

    public void setMove(GameMove move) {
        this.move = move;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

}
