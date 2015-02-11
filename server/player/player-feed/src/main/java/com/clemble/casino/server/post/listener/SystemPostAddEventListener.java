package com.clemble.casino.server.post.listener;

import com.clemble.casino.player.PlayerAware;
import com.clemble.casino.player.service.PlayerConnectionService;
import com.clemble.casino.post.PlayerPost;
import com.clemble.casino.server.event.post.SystemPostAddEvent;
import com.clemble.casino.server.player.notification.ServerNotificationService;
import com.clemble.casino.server.player.notification.SystemEventListener;
import com.clemble.casino.server.post.repository.PlayerPostRepository;

import java.util.ArrayList;
import java.util.Collection;

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
        // Step 0. Ignoring default player "casino"
        if (PlayerAware.DEFAULT_PLAYER.equals(event.getPost().getPlayer()))
            return;
        PlayerPost post = event.getPost();
        // Step 1. Saving post
        postRepository.save(post);
        // Step 2. Collecting interested parties
        Collection<String> players = new ArrayList<String>(connectionService.getConnections(post.getPlayer()));
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
