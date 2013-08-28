package com.gogomaya.server.player.lock;

import static com.google.common.base.Preconditions.checkNotNull;

final public class PlayerLockSupport {

    private static PlayerLockService lockService;

    public static PlayerLockService getPlayerLockService() {
        return checkNotNull(lockService);
    }

    @SuppressWarnings("static-access")
    public PlayerLockSupport(PlayerLockService lockService) {
        this.lockService = checkNotNull(lockService);
    }

}
