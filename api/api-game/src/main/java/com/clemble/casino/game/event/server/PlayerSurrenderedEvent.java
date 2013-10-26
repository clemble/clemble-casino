package com.clemble.casino.game.event.server;

import com.clemble.casino.game.GameSession;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.event.client.surrender.SurrenderEvent;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("playerGaveUp")
public class PlayerSurrenderedEvent<State extends GameState> extends GameServerEvent<State> {

    /**
     * Generated 07/05/13
     */
    private static final long serialVersionUID = 8613548852525073195L;

    final private SurrenderEvent reason;

    public PlayerSurrenderedEvent(GameSession<State> session, SurrenderEvent reason) {
        super(session);
        this.reason = reason;
    }

    @JsonCreator
    public PlayerSurrenderedEvent(@JsonProperty("session") GameSessionKey sessionKey,
            @JsonProperty("state") State state,
            @JsonProperty("reason") SurrenderEvent reason) {
        super(sessionKey, state);
        this.reason = reason;
    }

    public SurrenderEvent getReason() {
        return reason;
    }

    @Override
    public int hashCode() {
        return 31 * super.hashCode() + ((reason == null) ? 0 : reason.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PlayerSurrenderedEvent<?> other = (PlayerSurrenderedEvent<?>) obj;
        if (reason == null) {
            if (other.reason != null)
                return false;
        } else if (!reason.equals(other.reason))
            return false;
        return true;
    }

}
