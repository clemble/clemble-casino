package com.gogomaya.server.web.active;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.player.security.PlayerIdentity;
import com.gogomaya.server.repository.player.PlayerIdentityRepository;

@Service
public class PlayerIdentityVerificationService {

    @Autowired
    private PlayerIdentityRepository playerIdentityRepository;
    
    public void verify(PlayerIdentity playerIdentity) {
        // Step 1. Sanity check
        if(playerIdentity == null)
            throw GogomayaException.fromError(GogomayaError.IdentityInvalid);
        // Step 2. Simple comparison of identities
        PlayerIdentity associatedIdentity = playerIdentityRepository.findOne(playerIdentity.getPlayerId());
        if(!associatedIdentity.getSecret().equals(playerIdentity.getSecret()))
            throw GogomayaException.fromError(GogomayaError.IdentityInvalid);
    }
    
}
