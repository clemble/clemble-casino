package com.clemble.casino.player;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.social.connect.ConnectionKey;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("social")
public class SocialPlayerProfile extends NativePlayerProfile {

    /**
     * Generated 03/11/13
     */
    private static final long serialVersionUID = -3831720055626662655L;

    private Set<ConnectionKey> socialConnections = new HashSet<>();

    public SocialPlayerProfile() {
    }

    public Collection<ConnectionKey> getSocialConnections() {
        return socialConnections;
    }

    public SocialPlayerProfile setSocialConnections(Set<ConnectionKey> socialConnections) {
        this.socialConnections = socialConnections;
        return this;
    }

    public SocialPlayerProfile addSocialConnection(ConnectionKey newConnectionKey) {
        socialConnections.add(newConnectionKey);
        return this;
    }

}
