package com.clemble.casino.integration.player;

import com.clemble.casino.player.service.PlayerImageService;
import com.clemble.casino.server.profile.controller.PlayerImageServiceController;

/**
 * Created by mavarazy on 7/26/14.
 * TODO this is effectively non working, make some tests for this functionality
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
        return imageService.myImage();
    }

    @Override
    public byte[] mySmallImage() {
        return imageService.mySmallImage();
    }

    @Override
    public byte[] getImage(String player) {
        return imageService.getImage(player);
    }

    @Override
    public byte[] getSmallImage(String player) {
        return imageService.getSmallImage(player);
    }
}
