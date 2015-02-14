package com.clemble.casino.integration.player;

import com.clemble.casino.player.service.PlayerFeedService;
import com.clemble.casino.post.PlayerPost;
import com.clemble.casino.server.post.controller.PlayerFeedServiceController;
import com.clemble.casino.social.SocialProvider;

/**
 * Created by mavarazy on 12/1/14.
 */
public class IntegrationPlayerFeedService implements PlayerFeedService {

    final private String player;
    final private PlayerFeedServiceController feedService;

    public IntegrationPlayerFeedService(
        String player,
        PlayerFeedServiceController feedService) {
        this.player = player;
        this.feedService = feedService;
    }

    @Override
    public PlayerPost[] myFeed() {
        return feedService.myFeed(player);
    }

    @Override
    public PlayerPost[] getFeed(String player) {
        return feedService.getFeed(player);
    }

    @Override
    public PlayerPost share(String key, SocialProvider provider) {
        return feedService.share(player, key, provider);
    }
}
