package com.gogomaya.server.web.player.account;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.gogomaya.money.Currency;
import com.gogomaya.money.Money;
import com.gogomaya.payment.PlayerAccount;
import com.gogomaya.payment.service.PlayerAccountService;
import com.gogomaya.player.PlayerProfile;
import com.gogomaya.server.player.account.PlayerAccountServerService;
import com.gogomaya.server.repository.player.PlayerAccountRepository;
import com.gogomaya.web.payment.PaymentWebMapping;

@Controller
public class PlayerAccountController implements PlayerAccountService {

    final private PlayerAccountServerService playerAccountService;
    final private PlayerAccountRepository playerAccountRepository;

    public PlayerAccountController(PlayerAccountServerService playerAccountService,
            PlayerAccountRepository playerAccountRepository) {
        this.playerAccountService = checkNotNull(playerAccountService);
        this.playerAccountRepository = checkNotNull(playerAccountRepository);
    }

    @RequestMapping(method = RequestMethod.POST, value = PaymentWebMapping.PAYMENT_ACCOUNTS, produces = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED)
    public @ResponseBody PlayerAccount register(@RequestBody PlayerProfile playerProfile) {
        return playerAccountService.register(playerProfile);
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, value = PaymentWebMapping.PAYMENT_ACCOUNTS_PLAYER, produces = "application/json")
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody PlayerAccount get(@PathVariable("playerId") long playerWalletId) {
        // Step 1. Returning account from repository
        return playerAccountRepository.findOne(playerWalletId);
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
