package com.gogomaya.server.game.outcome;

import java.util.List;

import com.gogomaya.server.game.action.GameOutcome;
import com.gogomaya.server.game.action.GameSession;
import com.gogomaya.server.game.action.PlayerWonOutcome;
import com.gogomaya.server.game.rule.bet.FixedBetRule;
import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.game.tictactoe.action.TicTacToeState;
import com.gogomaya.server.money.Money;
import com.gogomaya.server.money.MoneySource;
import com.gogomaya.server.money.Operation;
import com.gogomaya.server.player.wallet.WalletOperation;
import com.gogomaya.server.player.wallet.WalletTransaction;
import com.gogomaya.server.player.wallet.WalletTransactionId;
import com.gogomaya.server.player.wallet.WalletTransactionManager;

public class TicTacToeOutcomeService implements GameOutcomeService<TicTacToeState> {

    final private WalletTransactionManager walletTransactionManager;

    public TicTacToeOutcomeService(WalletTransactionManager walletTransactionManager) {
        this.walletTransactionManager = walletTransactionManager;
    }

    @Override
    public void finished(GameSession<TicTacToeState> session) {
        GameOutcome outcome = session.getState().getOutcome();
        if (outcome instanceof PlayerWonOutcome) {
            long winnerId = ((PlayerWonOutcome) outcome).getWinner();
            List<Long> players = session.getPlayers();
            players.remove(winnerId);
            long looserId = players.iterator().next();

            GameSpecification specification = session.getSpecification();
            FixedBetRule fixedBetRule = specification.getBetRule();

            Money betSize = Money.create(specification.getCurrency(), fixedBetRule.getPrice());

            WalletTransactionId transactionId = new WalletTransactionId().setSource(MoneySource.TicTacToe).setTransactionId(session.getSessionId());
            WalletTransaction walletTransaction = new WalletTransaction().setTransactionId(transactionId)
                    .addWalletOperation(new WalletOperation().setAmmount(betSize).setOperation(Operation.Credit).setPlayerId(looserId))
                    .addWalletOperation(new WalletOperation().setAmmount(betSize).setOperation(Operation.Debit).setPlayerId(winnerId));

            walletTransactionManager.process(walletTransaction);
        }
    }

}
