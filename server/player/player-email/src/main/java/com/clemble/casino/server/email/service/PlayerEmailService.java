package com.clemble.casino.server.email.service;

import com.clemble.casino.server.email.PlayerEmail;

/**
 * Created by mavarazy on 12/6/14.
 */
public interface PlayerEmailService {

    public void requestVerification(PlayerEmail email);

}
