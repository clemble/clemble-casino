package com.gogomaya.server.game.event.server;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.event.client.surrender.SurrenderEvent;
import com.gogomaya.server.player.PlayerAware;

@JsonTypeName("playerGaveUp")
public class PlayerSurrenderedEvent<State extends GameState> extends GameServerEvent<State> implements PlayerAware {

    /**
     * Generated 07/05/13
     */
    private static final long serialVersionUID = 8613548852525073195L;

    private long playerId;

    private SurrenderEvent reason;

    public PlayerSurrenderedEvent() {
    }

    public PlayerSurrenderedEvent(State state) {
        this.setState(state);
    }

    @Override
    public long getPlayerId() {
        return playerId;
    }

    public PlayerSurrenderedEvent<State> setPlayerId(long newPlayerId) {
        this.playerId = newPlayerId;
        return this;
    }

    public SurrenderEvent getReason() {
        return reason;
    }

    public PlayerSurrenderedEvent<State> setReason(SurrenderEvent reason) {
        this.reason = reason;
        return this;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (playerId ^ (playerId >>> 32));
        result = prime * result + ((reason == null) ? 0 : reason.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PlayerSurrenderedEvent<State> other = (PlayerSurrenderedEvent<State>) obj;
        if (playerId != other.playerId)
            return false;
        if (reason == null) {
            if (other.reason != null)
                return false;
        } else if (!reason.equals(other.reason))
            return false;
        return true;
    }

}
