package com.clemble.casino.server.event.bet;

import com.clemble.casino.player.PlayerAware;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by mavarazy on 8/9/14.
 */
public class SystemBetCanceledEvent implements SystemBetEvent, PlayerAware {

    final public static String CHANNEL = "bet:canceled";

    final private String player;
    final private String betKey;

    @JsonCreator
    public SystemBetCanceledEvent(@JsonProperty("player") String player, @JsonProperty("betKey") String betKey) {
        this.player = player;
        this.betKey = betKey;
    }

    @Override
    public String getPlayer() {
        return player;
    }

    @Override
    public String toBetKey() {
        return betKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SystemBetCanceledEvent that = (SystemBetCanceledEvent) o;

        if (!betKey.equals(that.betKey)) return false;
        if (!player.equals(that.player)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = player.hashCode();
        result = 31 * result + betKey.hashCode();
        return result;
    }

    @Override
    public String getChannel() {
        return CHANNEL;
    }
}
