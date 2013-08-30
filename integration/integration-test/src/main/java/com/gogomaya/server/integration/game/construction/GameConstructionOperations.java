package com.gogomaya.server.integration.game.construction;

import java.util.Collection;

import com.gogomaya.event.ClientEvent;
import com.gogomaya.game.GameAware;
import com.gogomaya.game.GameState;
import com.gogomaya.game.configuration.GameSpecificationOptions;
import com.gogomaya.game.construct.GameRequest;
import com.gogomaya.game.specification.GameSpecification;
import com.gogomaya.server.integration.game.GameSessionPlayer;
import com.gogomaya.server.integration.player.Player;

public interface GameConstructionOperations<State extends GameState> extends GameAware {

    public GameSpecificationOptions getOptions();

    public GameSpecification selectSpecification(Player player);

    public GameSpecificationOptions getOptions(Player player);

    public ClientEvent constructionResponse(Player player, long requested, long construction);

    public GameSessionPlayer<State> constructAutomatic(Player player);

    public GameSessionPlayer<State> constructAutomatic(Player player, GameSpecification gameSpecification);

    public GameSessionPlayer<State> constructAvailability(Player player, GameSpecification specification, Collection<Long> participants);

    public GameSessionPlayer<State> construct(Player player, GameRequest request);

    public GameSessionPlayer<State> acceptInvitation(Player player, long construction);

    public void declineInvitation(Player player, long construction);

}
