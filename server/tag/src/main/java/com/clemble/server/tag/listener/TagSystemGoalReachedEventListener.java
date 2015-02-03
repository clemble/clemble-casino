package com.clemble.server.tag.listener;

import com.clemble.casino.player.service.PlayerNotificationService;
import com.clemble.casino.server.event.goal.SystemGoalReachedEvent;
import com.clemble.casino.server.player.notification.ServerNotificationService;
import com.clemble.casino.server.player.notification.SystemEventListener;
import com.clemble.casino.tag.event.ClembleTagAddedEvent;
import com.clemble.server.tag.ServerPlayerTags;
import com.clemble.server.tag.repository.ServerPlayerTagsRepository;

import javax.persistence.OptimisticLockException;

/**
 * Created by mavarazy on 2/3/15.
 */
public class TagSystemGoalReachedEventListener implements SystemEventListener<SystemGoalReachedEvent>{

    final private ServerPlayerTagsRepository tagsRepository;
    final private ServerNotificationService notificationService;

    public TagSystemGoalReachedEventListener(ServerPlayerTagsRepository tagsRepository, ServerNotificationService notificationService) {
        this.tagsRepository = tagsRepository;
        this.notificationService = notificationService;
    }

    @Override
    public void onEvent(SystemGoalReachedEvent event) {
        try {
            // Step 1. Fetching player tags
            ServerPlayerTags playerTags = tagsRepository.findOne(event.getPlayer());
            // Step 2. Add new tag to player tag
            tagsRepository.save(playerTags.add(event.getTag()));
            // Step 3. Send notification
            notificationService.send(event.getPlayer(), new ClembleTagAddedEvent(event.getTag()));
        } catch (OptimisticLockException e) {
            onEvent(event);
        }
    }

    @Override
    public String getChannel() {
        return SystemGoalReachedEvent.CHANNEL;
    }

    @Override
    public String getQueueName() {
        return SystemGoalReachedEvent.CHANNEL + " > tag";
    }
}
