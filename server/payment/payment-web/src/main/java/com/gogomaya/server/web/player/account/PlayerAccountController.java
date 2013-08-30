package com.gogomaya.server.web.player.account;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.gogomaya.error.GogomayaError;
import com.gogomaya.error.GogomayaException;
import com.gogomaya.money.Currency;
import com.gogomaya.money.Money;
import com.gogomaya.payment.PaymentTransaction;
import com.gogomaya.payment.PaymentTransactionId;
import com.gogomaya.payment.PlayerAccount;
import com.gogomaya.player.PlayerProfile;
import com.gogomaya.server.player.account.PlayerAccountService;
import com.gogomaya.server.repository.payment.PaymentTransactionRepository;
import com.gogomaya.server.repository.player.PlayerAccountRepository;
import com.gogomaya.web.payment.PaymentWebMapping;

@Controller
public class PlayerAccountController {

    final private PlayerAccountService playerAccountService;
    final private PlayerAccountRepository playerAccountRepository;
    final private PaymentTransactionRepository paymentTransactionRepository;

    public PlayerAccountController(PlayerAccountService playerAccountService,
            PlayerAccountRepository playerAccountRepository,
            PaymentTransactionRepository paymentTransactionRepository) {
        this.playerAccountService = checkNotNull(playerAccountService);
        this.playerAccountRepository = checkNotNull(playerAccountRepository);
        this.paymentTransactionRepository = checkNotNull(paymentTransactionRepository);
    }

    @RequestMapping(method = RequestMethod.POST, value = PaymentWebMapping.PAYMENT_ACCOUNTS, produces = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED)
    public @ResponseBody PlayerAccount register(@RequestBody PlayerProfile playerProfile) {
        return playerAccountService.register(playerProfile);
    }

    @RequestMapping(method = RequestMethod.GET, value = PaymentWebMapping.PAYMENT_ACCOUNTS_PLAYER, produces = "application/json")
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody PlayerAccount get(@RequestHeader("playerId") long playerId, @PathVariable("playerId") long playerWalletId) {
        // Step 1. Wallet can't be accessed someone other, then owner
        if (playerId != playerWalletId)
            throw GogomayaException.fromError(GogomayaError.PlayerAccountAccessDenied);
        // Step 2. Returning account from repository
        return playerAccountRepository.findOne(playerWalletId);
    }

    @RequestMapping(method = RequestMethod.GET, value = PaymentWebMapping.PAYMENT_ACCOUNTS_PLAYER_TRANSACTIONS, produces = "application/json")
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    List<PaymentTransaction> listPlayerTransaction(@RequestHeader("playerId") long requesterId, @PathVariable("playerId") long playerId) {
        // Step 1. Checking request/player identifier matches
        if (requesterId != playerId)
            throw GogomayaException.fromError(GogomayaError.PaymentTransactionAccessDenied);
        // Step 2. Sending transactions
        return paymentTransactionRepository.findByPaymentOperationsPlayerId(playerId);
    }

    @RequestMapping(method = RequestMethod.GET, value = PaymentWebMapping.PAYMENT_ACCOUNTS_PLAYER_TRANSACTIONS_TRANSACTION, produces = "application/json")
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
    

    @RequestMapping(method = RequestMethod.GET, value = PaymentWebMapping.PAYMENT_ACCOUNTS, produces = "application/json")
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody boolean canAfford(@RequestParam("player") String players, @RequestParam("currency") Currency currency, @RequestParam("amount") Long amount) {
        String[] splitPlayers = players.split(",");
        Collection<Long> parsedPlayers = new ArrayList<>(splitPlayers.length);
        for(String splitedPlayer: splitPlayers)
            parsedPlayers.add(Long.valueOf(splitedPlayer));
        return playerAccountService.canAfford(parsedPlayers, Money.create(currency, amount));
    }

}
