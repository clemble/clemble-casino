package com.clemble.casino.server.game.configuration;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.clemble.casino.game.Game;
import com.clemble.casino.game.configuration.GameSpecificationConfiguration;
import com.clemble.casino.game.configuration.GameSpecificationOptions;
import com.clemble.casino.game.configuration.SelectSpecificationOptions;
import com.clemble.casino.server.repository.game.GameSpecificationRepository;

public class GameSpecificationConfigurationManager implements GameSpecificationConfiguration {

    /**
     * Generated 13/07/13
     */
    private static final long serialVersionUID = -2558441579597068201L;

    final private Game game;
    final private GameSpecificationRepository specificationRepository;

    public GameSpecificationConfigurationManager(Game game, GameSpecificationRepository specificationRepository) {
        this.game = checkNotNull(game);
        this.specificationRepository = checkNotNull(specificationRepository);

        checkArgument(specificationRepository.findByGame(game).size() > 0);
    }

    @Override
    public Game getGame() {
        return game;
    }

    @Override
    public GameSpecificationOptions getSpecificationOptions() {
        return new SelectSpecificationOptions(Game.pic, specificationRepository.findByGame(game));
    }

}
