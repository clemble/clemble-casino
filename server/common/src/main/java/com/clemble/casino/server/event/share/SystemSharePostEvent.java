package com.clemble.casino.server.event.share;

import com.clemble.casino.goal.post.GoalPost;
import com.clemble.casino.player.PlayerAware;

/**
 * Created by mavarazy on 1/8/15.
 */
public class SystemSharePostEvent implements SystemShareEvent, PlayerAware {

    final public static String CHANNEL = "sys:share:post";

    final private String player;
    final private String providerId;
    final private GoalPost post;

    public SystemSharePostEvent(String player, String providerId, GoalPost post) {
        this.player = player;
        this.providerId = providerId;
        this.post = post;
    }

    @Override
    public String getPlayer() {
        return player;
    }

    public String getProviderId() {
        return providerId;
    }

    public GoalPost getPost() {
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

        SystemSharePostEvent that = (SystemSharePostEvent) o;

        if (!player.equals(that.player)) return false;
        if (!post.equals(that.post)) return false;
        if (!providerId.equals(that.providerId)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = player.hashCode();
        result = 31 * result + providerId.hashCode();
        result = 31 * result + post.hashCode();
        return result;
    }
}
