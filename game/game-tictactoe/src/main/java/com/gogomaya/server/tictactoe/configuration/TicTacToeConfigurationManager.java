package com.gogomaya.server.tictactoe.configuration;

import static com.google.common.base.Preconditions.checkNotNull;

import com.gogomaya.server.game.configuration.GameSpecificationConfiguration;
import com.gogomaya.server.game.configuration.GameSpecificationOptions;
import com.gogomaya.server.game.configuration.SelectSpecificationOptions;
import com.gogomaya.server.game.tictactoe.TicTacToe;
import com.gogomaya.server.repository.game.GameSpecificationRepository;

public class TicTacToeConfigurationManager implements GameSpecificationConfiguration {

    /**
     * Generated 13/07/13
     */
    private static final long serialVersionUID = -2558441579597068201L;

    final GameSpecificationRepository specificationRepository;

    public TicTacToeConfigurationManager(GameSpecificationRepository specificationRepository) {
        this.specificationRepository = checkNotNull(specificationRepository);
    }

    @Override
    public String getName() {
        return TicTacToe.NAME;
    }

    @Override
    public GameSpecificationOptions getSpecificationOptions() {
        return new SelectSpecificationOptions(TicTacToe.NAME, specificationRepository.findAll());
    }

}
