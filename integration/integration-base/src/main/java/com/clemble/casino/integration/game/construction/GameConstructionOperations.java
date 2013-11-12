package com.clemble.casino.integration.game.construction;

import java.util.Collection;

import com.clemble.casino.event.ClientEvent;
import com.clemble.casino.game.GameAware;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.configuration.GameSpecificationOptions;
import com.clemble.casino.game.construct.GameRequest;
import com.clemble.casino.game.specification.GameSpecification;
import com.clemble.casino.integration.game.GameSessionPlayer;
import com.clemble.casino.integration.player.Player;

public interface GameConstructionOperations<State extends GameState> extends GameAware {

    public GameSpecificationOptions getOptions();

    public GameSpecification selectSpecification(Player player);

    public GameSpecificationOptions getOptions(Player player);

    public ClientEvent constructionResponse(Player player, String requested, GameSessionKey construction);

    public GameSessionPlayer<State> constructAutomatic(Player player);

    public GameSessionPlayer<State> constructAutomatic(Player player, GameSpecification gameSpecification);

    public GameSessionPlayer<State> constructAvailability(Player player, GameSpecification specification, Collection<String> participants);

    public GameSessionPlayer<State> construct(Player player, GameRequest request);

    public GameSessionPlayer<State> acceptInvitation(Player player, GameSessionKey construction);

    public void declineInvitation(Player player, GameSessionKey construction);

}
