package com.gogomaya.server.game.outcome;

import java.util.Set;

import com.gogomaya.server.game.action.GameTable;
import com.gogomaya.server.game.rule.bet.FixedBetRule;
import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.game.tictactoe.action.TicTacToeState;
import com.gogomaya.server.money.Money;
import com.gogomaya.server.player.wallet.WalletTransactionManager;

public class TicTacToeOutcomeService implements GameOutcomeService<TicTacToeState> {

    final private WalletTransactionManager walletTransactionManager;

    public TicTacToeOutcomeService(WalletTransactionManager walletTransactionManager) {
        this.walletTransactionManager = walletTransactionManager;
    }

    @Override
    public void finished(GameTable<TicTacToeState> gameTable) {
        long winnerId = gameTable.getState().getWinner();

        GameSpecification specification = gameTable.getSpecification();
        FixedBetRule fixedBetRule = specification.getBetRule();

        Set<Long> players = gameTable.getCurrentSession().getPlayers();
        players.remove(winnerId);

        Money betSize = Money.create(specification.getCurrency(), fixedBetRule.getPrice());
        walletTransactionManager.debit(players.iterator().next(), gameTable.getState().getWinner(), betSize);
    }

}
