package com.gogomaya.server.web.player.wallet;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.payment.PaymentTransaction;
import com.gogomaya.server.payment.PaymentTransactionId;
import com.gogomaya.server.player.wallet.PlayerWallet;
import com.gogomaya.server.repository.payment.PaymentTransactionRepository;
import com.gogomaya.server.repository.player.PlayerWalletRepository;
import com.gogomaya.server.web.mapping.PaymentWebMapping;

@Controller
public class PlayerWalletController {

    final private PlayerWalletRepository walletRepository;
    final private PaymentTransactionRepository paymentTransactionRepository;

    public PlayerWalletController(PlayerWalletRepository walletRepository, PaymentTransactionRepository walletTransactionRepository) {
        this.walletRepository = checkNotNull(walletRepository);
        this.paymentTransactionRepository = checkNotNull(walletTransactionRepository);
    }

    @RequestMapping(method = RequestMethod.GET, value = PaymentWebMapping.WALLET_PLAYER, produces = "application/json")
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody PlayerWallet get(@RequestHeader("playerId") long playerId, @PathVariable("playerId") long playerWalletId) {
        // Step 1. Wallet can't be accessed someone other, then owner
        if (playerId != playerWalletId)
            throw GogomayaException.fromError(GogomayaError.PlayerWalletAccessDenied);
        // Step 2. Returning wallet repository
        return walletRepository.findOne(playerWalletId);
    }

    @RequestMapping(method = RequestMethod.GET, value = PaymentWebMapping.WALLET_PLAYER_TRANSACTIONS, produces = "application/json")
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody List<PaymentTransaction> listPlayerTransaction(@RequestHeader("playerId") long requesterId, @PathVariable("playerId") long playerId) {
        // Step 1. Checking request/player identifier matches
        if (requesterId != playerId)
            throw GogomayaException.fromError(GogomayaError.PaymentTransactionAccessDenied);
        // Step 2. Sending transactions
        return paymentTransactionRepository.findByWalletOperationsPlayerId(playerId);
    }

    @RequestMapping(method = RequestMethod.GET, value = PaymentWebMapping.WALLET_PLAYER_TRANSACTIONS_TRANSACTION, produces = "application/json")
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody PaymentTransaction getPlayerTransaction(
            @RequestHeader("playerId") long requesterId,
            @PathVariable("playerId") long playerId,
            @PathVariable("source") String source,
            @PathVariable("transactionId") long transactionId) {
        // Step 1. Checking request/player identifier matches
        if (requesterId != playerId)
            throw GogomayaException.fromError(GogomayaError.PaymentTransactionAccessDenied);
        // Step 2. Sending transactions
        PaymentTransaction transaction = paymentTransactionRepository.findOne(new PaymentTransactionId(source, transactionId));
        if (transaction == null)
            throw GogomayaException.fromError(GogomayaError.PaymentTransactionNotExists);
        // Step 3. Checking user was one of the participants in transaction
        if (!transaction.isParticipant(playerId))
            throw GogomayaException.fromError(GogomayaError.PaymentTransactionAccessDenied);
        // Step 4. Everything is fine returning transaction
        return transaction;
    }

}
