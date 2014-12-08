package com.clemble.casino.server.phone.controller;

import com.clemble.casino.player.service.PlayerPhoneService;

/**
 * Created by mavarazy on 12/8/14.
 */
public class PlayerPhoneServiceController implements PlayerPhoneService {


    @Override
    public boolean add(String phone) {
        return false;
    }

    @Override
    public boolean verify(String code) {
        return false;
    }
}
