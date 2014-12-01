package com.clemble.casino.server.event.post;

import com.clemble.casino.post.PlayerPost;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by mavarazy on 11/30/14.
 */
public class SystemPostAddEvent implements SystemPostEvent {

    final public static String CHANNEL = "sys:post:add";

    final private PlayerPost post;

    @JsonCreator
    public SystemPostAddEvent(@JsonProperty("post") PlayerPost post) {
        this.post = post;
    }

    @Override
    public PlayerPost getPost() {
        return post;
    }

    @Override
    public String getChannel() {
        return CHANNEL;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SystemPostAddEvent that = (SystemPostAddEvent) o;

        if (!post.equals(that.post)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return post.hashCode();
    }
}
