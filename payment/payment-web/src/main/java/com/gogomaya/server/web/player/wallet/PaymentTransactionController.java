package com.gogomaya.server.web.player.wallet;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.gogomaya.server.payment.PaymentTransaction;
import com.gogomaya.server.player.wallet.PlayerWalletService;
import com.gogomaya.server.web.mapping.PaymentWebMapping;

@Controller
public class PaymentTransactionController {

    final PlayerWalletService playerMoneyWalletManager;

    public PaymentTransactionController(final PlayerWalletService playerWalletManager) {
        this.playerMoneyWalletManager = checkNotNull(playerWalletManager);
    }

    @RequestMapping(method = RequestMethod.POST, value = PaymentWebMapping.WALLET_TRANSACTIONS, produces = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED)
    public @ResponseBody PaymentTransaction perform(PaymentTransaction paymentTransaction) {
        return null;
    }

}
