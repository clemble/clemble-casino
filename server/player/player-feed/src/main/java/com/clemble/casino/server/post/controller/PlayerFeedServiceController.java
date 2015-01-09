package com.clemble.casino.server.post.controller;

import com.clemble.casino.WebMapping;
import com.clemble.casino.goal.post.GoalPost;
import com.clemble.casino.player.PlayerPostWebMapping;
import com.clemble.casino.player.service.PlayerConnectionService;
import com.clemble.casino.player.service.PlayerFeedService;
import com.clemble.casino.post.PlayerPost;
import com.clemble.casino.server.event.share.SystemSharePostEvent;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import com.clemble.casino.server.post.repository.PlayerPostRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by mavarazy on 11/30/14.
 */
@RestController
public class PlayerFeedServiceController implements PlayerFeedService {

    final private PlayerPostRepository postRepository;
    final private PlayerConnectionService connectionService;
    final private SystemNotificationService notificationService;

    public PlayerFeedServiceController(
        PlayerConnectionService connectionService,
        PlayerPostRepository postRepository,
        SystemNotificationService notificationService) {
        this.postRepository = postRepository;
        this.connectionService = connectionService;
        this.notificationService = notificationService;
    }

    @Override
    public PlayerPost[] myFeed() {
        throw new UnsupportedOperationException();
    }

    @RequestMapping(method = RequestMethod.GET, value = PlayerPostWebMapping.MY_POSTS, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public PlayerPost[] myFeed(@CookieValue("player") String player) {
        // Step 1. Searching for connections
        Collection<String> connections = new ArrayList<String>(connectionService.getConnections(player));
        connections.add(player);
        // Step 2. Fetching player connections
        return postRepository.findByPlayerInOrderByCreatedDesc(connections).toArray(new PlayerPost[0]);
    }

    @Override
    public PlayerPost share(String key, String provider) {
        throw new UnsupportedOperationException();
    }

    @RequestMapping(method = RequestMethod.POST, value = PlayerPostWebMapping.POST_SHARE, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public PlayerPost share(@CookieValue("player")String player, @PathVariable("postKey") String key, @RequestBody String provider) {
        // Step 1. Fetching post
        // Fix this
        GoalPost post = (GoalPost) postRepository.findOne(key);
        // Step 2. Share post using provider.
        notificationService.send(new SystemSharePostEvent(player, provider, post));
        return post;
    }

}
