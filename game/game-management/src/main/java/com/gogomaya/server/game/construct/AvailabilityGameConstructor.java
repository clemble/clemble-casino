package com.gogomaya.server.game.construct;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Collection;

import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.error.GogomayaFailure;
import com.gogomaya.server.game.GameSession;
import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.GameTable;
import com.gogomaya.server.game.construct.AvailabilityGameRequest;
import com.gogomaya.server.game.session.GameSessionRepository;
import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.money.Money;
import com.gogomaya.server.player.state.PlayerStateManager;
import com.gogomaya.server.player.wallet.WalletTransactionManager;

public class AvailabilityGameConstructor<State extends GameState> implements GameConstructor<State, AvailabilityGameRequest> {

    final private WalletTransactionManager walletTransactionManager;
    final private PlayerStateManager playerStateManager;
    final private GameSessionRepository<State> sessionRepository;

    public AvailabilityGameConstructor(WalletTransactionManager walletTransactionManager, PlayerStateManager stateManager,
            GameSessionRepository<State> sessionRepository) {
        this.walletTransactionManager = checkNotNull(walletTransactionManager);
        this.playerStateManager = checkNotNull(stateManager);
        this.sessionRepository = checkNotNull(sessionRepository);
    }

    @Override
    public GameTable<State> construct(AvailabilityGameRequest request) {
        final Collection<Long> opponents = request.getOpponents();
        final GameSpecification specification = request.getSpecification();
        // Step 1. Trying to mark all players as busy
        GameSession<State> session = sessionRepository.saveAndFlush(new GameSession<State>().setSpecification(specification).setConstructor(request)
                .setPlayers(opponents));
        // Step 2. Checking if there are any busy players
        Collection<Long> busyPlayers = playerStateManager.markBusy(opponents, session.getSession());
        if (busyPlayers.size() != 0) {
            process(session.getSession());
        } else {
            playerStateManager.subscribe(busyPlayers, null);
        }
        return null;
    }

    public void process(long session) {
        AvailabilityGameRequest request = (AvailabilityGameRequest) sessionRepository.findOne(session).getConstructor();
        final Collection<Long> opponents = request.getOpponents();
        final GameSpecification specification = request.getSpecification();
        Money moneyNeeded = specification.extractMoneyNeeded();
        // Step 0. Checking player can afford to play this game
        Collection<GogomayaFailure> failures = new ArrayList<GogomayaFailure>();
        for (long opponent : opponents) {
            if (!walletTransactionManager.canAfford(opponent, moneyNeeded)) {
                failures.add(new GogomayaFailure(GogomayaError.GameConstructionInsufficientMoney, opponent));
            }
        }
        // Step 0.1. Throwing accumulated exception
        if (failures.size() > 0)
            throw GogomayaException.fromFailures(failures);
    }

}
