package com.gogomaya.server.web.player.wallet;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.gogomaya.server.player.wallet.WalletOperation;
import com.gogomaya.server.player.wallet.WalletTransactionManager;
import com.gogomaya.server.web.mapping.PaymentWebMapping;

@Controller
public class WalletTransactionController {

    final WalletTransactionManager playerMoneyWalletManager;

    public WalletTransactionController(final WalletTransactionManager playerWalletManager) {
        this.playerMoneyWalletManager = checkNotNull(playerWalletManager);
    }

    @RequestMapping(method = RequestMethod.POST, value = PaymentWebMapping.WALLET_TRANSACTION, produces = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED)
    public @ResponseBody
    WalletOperation debit(WalletOperation transaction) {
        return null;
    }

}
