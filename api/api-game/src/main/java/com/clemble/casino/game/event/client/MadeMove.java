package com.clemble.casino.game.event.client;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.hibernate.annotations.Type;

import com.clemble.casino.event.ClientEvent;

@Embeddable
public class MadeMove implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -7090751719767060121L;

    @Column(name = "MOVE_ID")
    private int moveId;

    @Type(type = "com.clemble.casino.event.ClientEventHibernate")
    @Column(name = "GAME_MOVE", length = 512)
    private ClientEvent move;

    @Column(name = "MOVE_TIME")
    private long processingTime;

    public int getMoveId() {
        return moveId;
    }

    public MadeMove setMoveId(int moveId) {
        this.moveId = moveId;
        return this;
    }

    public ClientEvent getMove() {
        return move;
    }

    public MadeMove setMove(ClientEvent move) {
        this.move = move;
        return this;
    }

    public long getProcessingTime() {
        return processingTime;
    }

    public MadeMove setProcessingTime(long processingTime) {
        this.processingTime = processingTime;
        return this;
    }

    @Override
    public int hashCode() {
        return moveId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MadeMove other = (MadeMove) obj;
        if (move == null) {
            if (other.move != null)
                return false;
        } else if (!move.equals(other.move))
            return false;
        if (moveId != other.moveId)
            return false;
        if (processingTime != other.processingTime)
            return false;
        return true;
    }

    public static MadeMove[] sort(Collection<MadeMove> madeMoves) {
        // Step 1. Creating sortedMove array
        MadeMove[] sortedMoves = new MadeMove[madeMoves.size()];
        // Step 2. Putting all made moves to the array
        for (MadeMove madeMove : madeMoves) {
            sortedMoves[madeMove.getMoveId()] = madeMove;
        }
        // Step 3. Returning sorted array
        return sortedMoves;
    }

}
