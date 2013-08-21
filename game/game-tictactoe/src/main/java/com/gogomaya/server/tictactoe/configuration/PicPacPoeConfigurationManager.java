package com.gogomaya.server.tictactoe.configuration;

import static com.google.common.base.Preconditions.checkNotNull;

import com.gogomaya.server.game.Game;
import com.gogomaya.server.game.configuration.GameSpecificationConfiguration;
import com.gogomaya.server.game.configuration.GameSpecificationOptions;
import com.gogomaya.server.game.configuration.SelectSpecificationOptions;
import com.gogomaya.server.repository.game.GameSpecificationRepository;

public class PicPacPoeConfigurationManager implements GameSpecificationConfiguration {

    /**
     * Generated 13/07/13
     */
    private static final long serialVersionUID = -2558441579597068201L;

    final GameSpecificationRepository specificationRepository;

    public PicPacPoeConfigurationManager(GameSpecificationRepository specificationRepository) {
        this.specificationRepository = checkNotNull(specificationRepository);
    }

    @Override
    public Game getGame() {
        return Game.pic;
    }

    @Override
    public GameSpecificationOptions getSpecificationOptions() {
        return new SelectSpecificationOptions(Game.pic, specificationRepository.findAll());
    }

}
