package com.gogomaya.player.service;

import com.gogomaya.player.SocialConnectionData;

public interface PlayerSocialProfileService {

    public SocialConnectionData add(long playerId, SocialConnectionData socialConnectionData);

}
