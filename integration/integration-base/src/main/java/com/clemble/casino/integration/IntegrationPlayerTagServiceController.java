package com.clemble.casino.integration;

import com.clemble.casino.tag.ClembleTag;
import com.clemble.casino.tag.service.PlayerTagService;
import com.clemble.server.tag.controller.PlayerTagServiceController;

import java.util.Set;

/**
 * Created by mavarazy on 2/3/15.
 */
public class IntegrationPlayerTagServiceController implements PlayerTagService {

    final private String player;
    final private PlayerTagServiceController tagServiceController;

    public IntegrationPlayerTagServiceController(String player, PlayerTagServiceController tagServiceController) {
        this.player = player;
        this.tagServiceController = tagServiceController;
    }

    @Override
    public Set<ClembleTag> myTags() {
        return tagServiceController.myTags(player);
    }

    @Override
    public Set<ClembleTag> getTags(String player) {
        return tagServiceController.getTags(player);
    }

}
