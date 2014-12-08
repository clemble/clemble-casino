package com.clemble.casino.server.phone;

import com.clemble.casino.player.PlayerAware;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;

/**
 * Created by mavarazy on 12/8/14.
 */
public class PendingPlayerPhone implements PlayerAware {

    @Id
    final private String player;
    final private String phone;
    final private String code;

    @JsonCreator
    public PendingPlayerPhone(
        @JsonProperty("player") String player,
        @JsonProperty("phone") String phone,
        @JsonProperty("code") String code) {
        this.player = player;
        this.phone = phone;
        this.code = code;
    }

    @Override
    public String getPlayer() {
        return player;
    }

    public String getPhone() {
        return phone;
    }

    public String getCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PendingPlayerPhone that = (PendingPlayerPhone) o;

        if (!code.equals(that.code)) return false;
        if (!phone.equals(that.phone)) return false;
        if (!player.equals(that.player)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = player.hashCode();
        result = 31 * result + phone.hashCode();
        result = 31 * result + code.hashCode();
        return result;
    }

}
