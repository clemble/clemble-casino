package com.clemble.casino.server.post;

import com.clemble.casino.player.PlayerAware;
import com.clemble.casino.post.PlayerPost;
import com.clemble.casino.post.PlayerPostAware;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Created by mavarazy on 12/1/14.
 */
public class ServerPlayerPost implements PlayerPostAware, PlayerAware {

    final private String key;
    final private String player;
    final private PlayerPost post;
    final private Date created;

    @JsonCreator
    public ServerPlayerPost(
        @JsonProperty("key") String key,
        @JsonProperty(PLAYER) String player,
        @JsonProperty("post") PlayerPost post,
        @JsonProperty("created") Date created) {
        this.key = key;
        this.player = player;
        this.post = post;
        this.created = created;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String getPlayer() {
        return player;
    }

    @Override
    public PlayerPost getPost() {
        return post;
    }

    public Date getCreated() {
        return created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ServerPlayerPost that = (ServerPlayerPost) o;

        if (!created.equals(that.created)) return false;
        if (!key.equals(that.key)) return false;
        if (!player.equals(that.player)) return false;
        if (!post.equals(that.post)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = key.hashCode();
        result = 31 * result + player.hashCode();
        result = 31 * result + post.hashCode();
        result = 31 * result + created.hashCode();
        return result;
    }

}
