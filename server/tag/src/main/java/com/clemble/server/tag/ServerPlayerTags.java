package com.clemble.server.tag;

import com.clemble.casino.player.PlayerAware;
import com.clemble.casino.tag.ClembleTag;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;

import java.util.Set;

/**
 * Created by mavarazy on 2/3/15.
 */
public class ServerPlayerTags implements PlayerAware {

    @Id
    final private String player;
    final private Set<ClembleTag> tags;

    @JsonCreator
    public ServerPlayerTags(@JsonProperty(PLAYER) String player, @JsonProperty("tags") Set<ClembleTag> tags) {
        this.player = player;
        this.tags = tags;
    }

    public String getPlayer() {
        return player;
    }

    public Set<ClembleTag> getTags() {
        return tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServerPlayerTags)) return false;

        ServerPlayerTags that = (ServerPlayerTags) o;

        if (player != null ? !player.equals(that.player) : that.player != null) return false;
        if (tags != null ? !tags.equals(that.tags) : that.tags != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = player != null ? player.hashCode() : 0;
        result = 31 * result + (tags != null ? tags.hashCode() : 0);
        return result;
    }
}
