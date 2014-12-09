package com.clemble.casino.server.phone.controller;

import com.clemble.casino.player.PlayerPhone;
import com.clemble.casino.player.PlayerPhoneVerification;
import com.clemble.casino.player.PlayerPhoneWebMapping;
import com.clemble.casino.player.service.PlayerPhoneService;
import com.clemble.casino.server.phone.service.ServerPlayerPhoneService;
import org.springframework.web.bind.annotation.*;

/**
 * Created by mavarazy on 12/8/14.
 */
@RestController
public class PlayerPhoneServiceController implements PlayerPhoneService {

    final ServerPlayerPhoneService playerPhoneService;

    public PlayerPhoneServiceController(ServerPlayerPhoneService playerPhoneService) {
        this.playerPhoneService = playerPhoneService;
    }

    @Override
    public boolean add(PlayerPhone phone) {
        throw new UnsupportedOperationException();
    }

    @RequestMapping(method = RequestMethod.POST, value = PlayerPhoneWebMapping.MY)
    public boolean add(@CookieValue("player") String me, @RequestBody PlayerPhone phone) {
        return playerPhoneService.add(me, phone.getPhone());
    }

    @Override
    public boolean remove() {
        throw new UnsupportedOperationException();
    }

    @RequestMapping(method = RequestMethod.DELETE, value = PlayerPhoneWebMapping.MY)
    public boolean remove(@CookieValue("player") String me) {
        return playerPhoneService.remove(me);
    }

    @Override
    public boolean verify(PlayerPhoneVerification code) {
        throw new UnsupportedOperationException();
    }

    @RequestMapping(method = RequestMethod.POST, value = PlayerPhoneWebMapping.MY_VERIFY)
    public boolean verify(@CookieValue("player") String me, @RequestBody PlayerPhoneVerification code) {
        return playerPhoneService.verify(me, code.getCode());
    }

}
