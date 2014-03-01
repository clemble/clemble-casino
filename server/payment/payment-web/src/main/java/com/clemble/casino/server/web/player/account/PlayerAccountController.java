package com.clemble.casino.server.web.player.account;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.clemble.casino.payment.PlayerAccount;
import com.clemble.casino.payment.money.Currency;
import com.clemble.casino.payment.money.Money;
import com.clemble.casino.payment.service.PlayerAccountService;
import com.clemble.casino.server.ExternalController;
import com.clemble.casino.server.player.account.ServerPlayerAccountService;
import com.clemble.casino.server.repository.payment.PlayerAccountTemplate;
import com.clemble.casino.web.mapping.WebMapping;
import com.clemble.casino.web.payment.PaymentWebMapping;

@Controller
public class PlayerAccountController implements PlayerAccountService, ExternalController {

    final private ServerPlayerAccountService playerAccountService;
    final private PlayerAccountTemplate accountTemplate;

    public PlayerAccountController(ServerPlayerAccountService playerAccountService,
            PlayerAccountTemplate playerAccountRepository) {
        this.playerAccountService = checkNotNull(playerAccountService);
        this.accountTemplate = checkNotNull(playerAccountRepository);
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, value = PaymentWebMapping.PAYMENT_ACCOUNTS_PLAYER, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody PlayerAccount get(@PathVariable("player") String playerWalletId) {
        // Step 1. Returning account from repository
        return accountTemplate.findOne(playerWalletId);
    }

    
    // TODO Normalize this behavior
    @RequestMapping(method = RequestMethod.GET, value = PaymentWebMapping.PAYMENT_ACCOUNTS, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody List<String> canAfford(@RequestParam("player") String players, @RequestParam("currency") Currency currency, @RequestParam("amount") Long amount) {
        String[] splitPlayers = players.split(",");
        Collection<String> parsedPlayers = new ArrayList<>(splitPlayers.length);
        for(String splitedPlayer: splitPlayers)
            parsedPlayers.add(splitedPlayer);
        return playerAccountService.canAfford(parsedPlayers, Money.create(currency, amount));
    }

}
