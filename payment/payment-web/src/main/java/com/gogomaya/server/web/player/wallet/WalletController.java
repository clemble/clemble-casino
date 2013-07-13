package com.gogomaya.server.web.player.wallet;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.gogomaya.server.player.wallet.PlayerWallet;
import com.gogomaya.server.repository.payment.PlayerWalletRepository;
import com.gogomaya.server.web.mapping.PaymentWebMapping;

@Controller
public class WalletController {

    final private PlayerWalletRepository walletRepository;

    public WalletController(PlayerWalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @RequestMapping(method = RequestMethod.GET, value = PaymentWebMapping.WALLET_PLAYER, produces = "application/json")
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody PlayerWallet get(@RequestHeader("playerId") long playerId, @PathVariable("playerId") long playerWalletId) {
        return walletRepository.findOne(playerWalletId);
    }

}
