package com.clemble.casino.server.game.configuration;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.List;

import com.clemble.casino.game.configuration.ServerGameConfiguration;
import com.clemble.casino.game.service.GameConfigurationService;
import com.clemble.casino.game.specification.GameConfiguration;
import com.clemble.casino.game.specification.MatchGameConfiguration;
import com.clemble.casino.game.specification.PotGameConfiguration;
import com.clemble.casino.game.specification.TournamentGameConfiguration;
import com.clemble.casino.server.repository.game.ServerGameConfigurationRepository;

public class ServerGameConfigurationService implements GameConfigurationService {

    final private ServerGameConfigurationRepository specificationRepository;

    public ServerGameConfigurationService(ServerGameConfigurationRepository specificationRepository) {
        this.specificationRepository = checkNotNull(specificationRepository);
    }

    public boolean isValid(GameConfiguration configuration) {
        return true;
    }

    @Override
    public List<MatchGameConfiguration> getMatchConfigurations() {
        // TODO OPTIMIZE
        return filter(MatchGameConfiguration.class);
    }

    @Override
    public List<PotGameConfiguration> getPotConfigurations() {
        // TODO OPTIMIZE
        return filter(PotGameConfiguration.class);
    }

    @Override
    public List<TournamentGameConfiguration> getTournamentConfigurations() {
        // TODO OPTIMIZE
        return filter(TournamentGameConfiguration.class);
    }
    
    private <T extends GameConfiguration> List<T> filter(Class<T> target) {
        List<ServerGameConfiguration> configurations = specificationRepository.findAll();
        List<T> matchConfigurations = new ArrayList<>();
        for(ServerGameConfiguration configuration: configurations)
            if(target.isAssignableFrom(configuration.getConfiguration().getClass()))
                matchConfigurations.add((T) configuration.getConfiguration());
        return matchConfigurations;
    }

}
