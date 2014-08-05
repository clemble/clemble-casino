package com.clemble.casino.integration.player;

import com.clemble.casino.player.service.PlayerImageService;
import com.clemble.casino.server.profile.controller.PlayerImageServiceController;

/**
 * Created by mavarazy on 7/26/14.
 */
public class IntegrationPlayerImageService implements PlayerImageService {

    final private String player;
    final private PlayerImageServiceController imageService;

    public IntegrationPlayerImageService(String player, PlayerImageServiceController imageService) {
        this.player = player;
        this.imageService = imageService;
    }

    @Override
    public byte[] myImage() {
        return imageService.myImage(player);
    }

    @Override
    public byte[] getImage(String player) {
        return imageService.getImage(player);
    }
}
