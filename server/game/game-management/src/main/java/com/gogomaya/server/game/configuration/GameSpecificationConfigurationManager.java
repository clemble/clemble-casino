package com.gogomaya.server.game.configuration;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.gogomaya.game.Game;
import com.gogomaya.game.configuration.GameSpecificationConfiguration;
import com.gogomaya.game.configuration.GameSpecificationOptions;
import com.gogomaya.game.configuration.SelectSpecificationOptions;
import com.gogomaya.server.repository.game.GameSpecificationRepository;

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
