package com.clemble.casino.server.event.bet;

import com.clemble.casino.player.PlayerAware;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Created by mavarazy on 8/9/14.
 */
public class SystemBetCompletedEvent implements SystemBetEvent, PlayerAware {

    final public static String CHANNEL = "bet:completed";

    final private String betKey;
    final private String player;

    @JsonCreator
    public SystemBetCompletedEvent(@JsonProperty("betKey") String betKey, @JsonProperty("player") String player) {
        this.betKey = betKey;
        this.player = player;
    }

    @Override
    public String toBetKey() {
        return betKey;
    }

    @Override
    public String getPlayer() {
        return player;
    }

    @Override
    public String getChannel() {
        return CHANNEL;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SystemBetCompletedEvent that = (SystemBetCompletedEvent) o;

        if (!betKey.equals(that.betKey)) return false;
        if (!player.equals(that.player)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = betKey.hashCode();
        result = 31 * result + player.hashCode();
        return result;
    }

}
