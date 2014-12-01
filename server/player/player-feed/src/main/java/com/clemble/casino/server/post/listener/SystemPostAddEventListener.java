package com.clemble.casino.server.post.listener;

import com.clemble.casino.goal.post.GoalPost;
import com.clemble.casino.player.service.PlayerConnectionService;
import com.clemble.casino.post.PlayerPost;
import com.clemble.casino.server.event.post.SystemPostAddEvent;
import com.clemble.casino.server.player.notification.ServerNotificationService;
import com.clemble.casino.server.player.notification.SystemEventListener;
import com.clemble.casino.server.post.ServerPlayerPost;
import com.clemble.casino.server.post.repository.PlayerPostRepository;

import java.util.Collection;
import java.util.Date;

/**
 * Created by mavarazy on 11/30/14.
 */
public class SystemPostAddEventListener implements SystemEventListener<SystemPostAddEvent> {

    final private PlayerPostRepository postRepository;
    final private PlayerConnectionService connectionService;
    final private ServerNotificationService notificationService;

    public SystemPostAddEventListener(
        PlayerPostRepository postRepository,
        PlayerConnectionService connectionService,
        ServerNotificationService notificationService
    ) {
        this.postRepository = postRepository;
        this.connectionService = connectionService;
        this.notificationService = notificationService;
    }

    @Override
    public void onEvent(SystemPostAddEvent event) {
        PlayerPost post = event.getPost();
        String key = post instanceof GoalPost ? ((GoalPost) post).getGoalKey() : null;
        // Step 1. Saving post
        postRepository.save(new ServerPlayerPost(key, post.getPlayer(), post, new Date()));
        // Step 2. Collecting interested parties
        Collection<String> players = connectionService.getConnections(post.getPlayer());
        players.add(post.getPlayer());
        // Step 3. Sending notifications to all
        notificationService.send(players, post);
    }

    @Override
    public String getChannel() {
        return SystemPostAddEvent.CHANNEL;
    }

    @Override
    public String getQueueName() {
        return SystemPostAddEvent.CHANNEL + " > player:post";
    }

}