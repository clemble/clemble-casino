package com.clemble.server.tag.listener;

import com.clemble.casino.server.event.player.SystemPlayerCreatedEvent;
import com.clemble.casino.server.player.notification.SystemEventListener;
import com.clemble.server.tag.ServerPlayerTags;
import com.clemble.server.tag.repository.ServerPlayerTagsRepository;

import java.util.Collections;

/**
 * Created by mavarazy on 2/3/15.
 */
public class TagSystemPlayerCreatedEventListener implements SystemEventListener<SystemPlayerCreatedEvent>{

    final private ServerPlayerTagsRepository tagsRepository;

    public TagSystemPlayerCreatedEventListener(ServerPlayerTagsRepository tagsRepository) {
        this.tagsRepository = tagsRepository;
    }

    @Override
    public void onEvent(SystemPlayerCreatedEvent event) {
        // Step 1. Fetching player tags
        ServerPlayerTags playerTags = tagsRepository.findOne(event.getPlayer());
        // Step 2. Checking player tags exist
        if (playerTags != null)
            return;
        // Step 3. Going through player tags
        tagsRepository.save(new ServerPlayerTags(event.getPlayer(), Collections.emptySet(), null));
    }

    @Override
    public String getChannel() {
        return SystemPlayerCreatedEvent.CHANNEL;
    }

    @Override
    public String getQueueName() {
        return SystemPlayerCreatedEvent.CHANNEL + " > tag";
    }

}
