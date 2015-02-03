package com.clemble.server.tag;

import com.clemble.casino.VersionAware;
import com.clemble.casino.player.PlayerAware;
import com.clemble.casino.tag.ClembleTag;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;

import java.util.Set;
import java.util.TreeSet;

/**
 * Created by mavarazy on 2/3/15.
 */
public class ServerPlayerTags implements PlayerAware, VersionAware {

    @Id
    final private String player;
    final private Set<ClembleTag> tags;
    final private Integer version;

    @JsonCreator
    public ServerPlayerTags(@JsonProperty(PLAYER) String player, @JsonProperty("tags") Set<ClembleTag> tags, @JsonProperty("version") Integer version) {
        this.version = version;
        this.player = player;
        this.tags = tags;
    }

    @Override
    public String getPlayer() {
        return player;
    }

    public ClembleTag getTag(String tagText) {
        for(ClembleTag tag: tags) {
            if (tag.getTag().equals(tagText)) {
                return tag;
            }
        }
        return null;
    }

    public ServerPlayerTags add(String tag) {
        // Step 1. Checking tag exists
        ClembleTag existingTag = getTag(tag);
        if (existingTag == null) {
            // Case 1. No such tag exists
            tags.add(new ClembleTag(tag, 1));
        } else {
            tags.remove(existingTag);
            tags.add(new ClembleTag(tag, existingTag.getPower() + 1));
        }
        return this;
    }

    public Set<ClembleTag> getTags() {
        return tags;
    }

    @Override
    public Integer getVersion() {
        return version;
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
