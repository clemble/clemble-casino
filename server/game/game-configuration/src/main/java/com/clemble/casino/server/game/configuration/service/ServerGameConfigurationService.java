package com.clemble.casino.server.game.configuration.service;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import com.clemble.casino.server.game.configuration.repository.GameConfigurationRepository;
import com.clemble.casino.game.lifecycle.configuration.service.GameConfigurationService;
import com.clemble.casino.game.lifecycle.configuration.GameConfiguration;

public class ServerGameConfigurationService implements GameConfigurationService {

    final private GameConfigurationRepository specificationRepository;

    public ServerGameConfigurationService(GameConfigurationRepository specificationRepository) {
        this.specificationRepository = checkNotNull(specificationRepository);
    }

    public boolean isValid(GameConfiguration configuration) {
        return true;
    }

    @Override
    public List<GameConfiguration> getConfigurations() {
        return specificationRepository.findAll();
    }


}
