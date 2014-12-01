package com.clemble.casino.server.post.controller;

import com.clemble.casino.WebMapping;
import com.clemble.casino.player.PlayerPostWebMapping;
import com.clemble.casino.player.service.PlayerConnectionService;
import com.clemble.casino.player.service.PlayerFeedService;
import com.clemble.casino.post.PlayerPost;
import com.clemble.casino.server.post.repository.PlayerPostRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Created by mavarazy on 11/30/14.
 */
public class PlayerFeedServiceController implements PlayerFeedService {

    final private PlayerPostRepository postRepository;
    final private PlayerConnectionService connectionService;

    public PlayerFeedServiceController(PlayerConnectionService connectionService, PlayerPostRepository postRepository) {
        this.postRepository = postRepository;
        this.connectionService = connectionService;
    }

    @Override
    public PlayerPost[] myFeed() {
        throw new UnsupportedOperationException();
    }

    @RequestMapping(method = RequestMethod.GET, value = PlayerPostWebMapping.MY_POSTS, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public PlayerPost[] myFeed(@CookieValue("player") String player) {
        // Step 1. Searching for connections
        Collection<String> connections = connectionService.getConnections(player);
        connections.add(player);
        // Step 2. Fetching player connections
        return postRepository.
            findByPlayerInOrderByCreated(connections).
            stream().
            map(sps -> sps.getPost()).
            collect(Collectors.toList()).toArray(new PlayerPost[0]);
    }

}
