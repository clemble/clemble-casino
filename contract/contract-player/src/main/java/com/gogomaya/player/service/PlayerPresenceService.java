package com.gogomaya.player.service;

import java.util.List;

import com.gogomaya.player.PlayerPresence;

public interface PlayerPresenceService {

    public PlayerPresence getPresence(long player);

    public List<PlayerPresence> getPresences(List<Long> players);

}
