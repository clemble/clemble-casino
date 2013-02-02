package com.gogomaya.server.social;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;

import com.gogomaya.server.user.GamerProfile;
import com.gogomaya.server.user.GamerProfileRepository;

public class SocialGamerProfileCreator implements ConnectionSignUp {

    @Autowired
    final private GamerProfileRepository gamerProfileRepository;
    
    public SocialGamerProfileCreator(GamerProfileRepository gamerProfileRepository) {
        this.gamerProfileRepository = checkNotNull(gamerProfileRepository);
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public String execute(Connection<?> connection) {
        // Step 1. Checking provided Connection<?> valid
        if (connection == null)
            throw new IllegalArgumentException("No Connection defined.");
        // Step 2. Retrieving SocialAdapter for the Connection
        SocialAdapter socialAdapter = SocialAdapter.getSocialAdapter(connection.getKey().getProviderId());
        if(socialAdapter == null)
            throw new IllegalArgumentException("No SocialAdapter exists for Connection");
        // Step 3. Generating gamer profile based on SocialConnection
        GamerProfile gamerProfile = socialAdapter.fetchGamerProfile(connection.getApi());
        gamerProfile = gamerProfileRepository.saveAndFlush(gamerProfile);
        // Step 4. Returning user identifier
        return gamerProfile.getUserId();
    }

}
