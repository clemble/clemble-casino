package com.clemble.server.tag.listener;

import com.clemble.casino.goal.lifecycle.management.GoalState;
import com.clemble.casino.server.event.goal.SystemGoalReachedEvent;
import com.clemble.casino.server.player.notification.ServerNotificationService;
import com.clemble.casino.server.player.notification.SystemEventListener;
import com.clemble.casino.tag.TagAware;
import com.clemble.casino.tag.event.TagCreatedEvent;
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
        GoalState state = event.getState();
        // Step 1. Adding tags for victory
        addTag(event.getPlayer(), state.getTag());
        addTag(event.getPlayer(), TagAware.VICTORY_TAG);
        // Step 2. Adding tags for support
        for(String supporter: state.getSupporters()) {
            addTag(supporter, TagAware.SUPPORTER_TAG);
        }
    }

    private void addTag(String player, String tag) {
        try {
            // Step 1. Fetching player tags
            ServerPlayerTags playerTags = tagsRepository.findOne(player);
            // Step 2. Add new tag to player tag
            tagsRepository.save(playerTags.add(tag));
            // Step 3. Send notification
            notificationService.send(player, new TagCreatedEvent(tag));
        } catch (OptimisticLockException e) {
            addTag(player, tag);
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
