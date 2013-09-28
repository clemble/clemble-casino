package com.gogomaya.player.service;

import java.util.List;

import com.gogomaya.player.PlayerPresence;

public interface PlayerPresenceService {

    public PlayerPresence getPresence(String player);

    public List<PlayerPresence> getPresences(List<String> players);

}
