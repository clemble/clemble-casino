package com.clemble.casino.server.registration;

import com.clemble.casino.player.EmailAware;
import com.clemble.casino.player.PlayerAware;
import com.clemble.casino.player.PlayerNickNameAware;
import com.clemble.casino.registration.PlayerCredential;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

/**
 * Created by mavarazy on 11/1/14.
 */
public class ServerPlayerCredential implements PlayerAware, PlayerNickNameAware, EmailAware, Serializable {

    @Id
    final private String player;
    final private String email;
    final private String nickName;
    final private String hash;

    @JsonCreator
    public ServerPlayerCredential(
        @JsonProperty("player") String player,
        @JsonProperty("email") String email,
        @JsonProperty("nickName") String nickName,
        @JsonProperty("hash") String hash) {
        this.player = player;
        this.email = email;
        this.nickName = nickName;
        this.hash = hash;
    }

    @Override
    public String getPlayer() {
        return player;
    }

    @Override
    public String getNickName() {
        return nickName;
    }

    public String getHash() {
        return hash;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ServerPlayerCredential that = (ServerPlayerCredential) o;

        if (!email.equals(that.email)) return false;
        if (!hash.equals(that.hash)) return false;
        if (!player.equals(that.player)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = player.hashCode();
        result = 31 * result + email.hashCode();
        result = 31 * result + hash.hashCode();
        return result;
    }

}
