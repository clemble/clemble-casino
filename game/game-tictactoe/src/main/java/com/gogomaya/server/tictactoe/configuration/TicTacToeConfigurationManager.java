package com.gogomaya.server.tictactoe.configuration;

import static com.google.common.base.Preconditions.checkNotNull;

import com.gogomaya.server.game.configuration.GameConfigurationManager;
import com.gogomaya.server.game.configuration.GameSpecificationOptions;
import com.gogomaya.server.game.configuration.SelectSpecificationOptions;
import com.gogomaya.server.game.tictactoe.TicTacToe;
import com.gogomaya.server.repository.game.GameSpecificationRepository;

public class TicTacToeConfigurationManager implements GameConfigurationManager {

    final GameSpecificationRepository specificationRepository;

    public TicTacToeConfigurationManager(GameSpecificationRepository specificationRepository) {
        this.specificationRepository = checkNotNull(specificationRepository);
    }

    public GameSpecificationOptions getSpecificationOptions() {
        return new SelectSpecificationOptions(TicTacToe.NAME, specificationRepository.findAll());
    }

}
