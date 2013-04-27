package com.gogomaya.server.web.player.wallet;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.gogomaya.server.money.PlayerMoneyTransaction;
import com.gogomaya.server.player.wallet.PlayerTransactionManager;

@Controller
public class WalletTransactionController {

    final PlayerTransactionManager playerMoneyWalletManager;

    public WalletTransactionController(final PlayerTransactionManager playerWalletManager) {
        this.playerMoneyWalletManager = checkNotNull(playerWalletManager);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/player/wallet/transaction", produces = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED)
    public @ResponseBody
    PlayerMoneyTransaction debit(PlayerMoneyTransaction transaction) {
        return null;
    }

}
