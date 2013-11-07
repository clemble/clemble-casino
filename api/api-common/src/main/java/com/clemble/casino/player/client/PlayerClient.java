package com.clemble.casino.player.client;

import org.springframework.data.annotation.Id;

import com.clemble.casino.player.PlayerAware;

public class PlayerClient implements PlayerAware, ClientAware {

    /**
     * Generate 01/11/13
     */
    private static final long serialVersionUID = -8353426338769515187L;

    @Id
    final private String client;
    final private String player;
    final private SignatureType signatureType;
    final private String clientSecret;

    public PlayerClient(String player, String client, SignatureType signatureAlgorithm, String clientSecret) {
        this.player = player;
        this.client = client;
        this.signatureType = signatureAlgorithm;
        this.clientSecret = clientSecret;
    }

    @Override
    public String getPlayer() {
        return player;
    }

    public String getClient() {
        return client;
    }

    public SignatureType getSignatureAlgorithm() {
        return signatureType;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((client == null) ? 0 : client.hashCode());
        result = prime * result + ((clientSecret == null) ? 0 : clientSecret.hashCode());
        result = prime * result + ((player == null) ? 0 : player.hashCode());
        result = prime * result + ((signatureType == null) ? 0 : signatureType.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PlayerClient other = (PlayerClient) obj;
        if (client == null) {
            if (other.client != null)
                return false;
        } else if (!client.equals(other.client))
            return false;
        if (clientSecret == null) {
            if (other.clientSecret != null)
                return false;
        } else if (!clientSecret.equals(other.clientSecret))
            return false;
        if (player == null) {
            if (other.player != null)
                return false;
        } else if (!player.equals(other.player))
            return false;
        if (signatureType != other.signatureType)
            return false;
        return true;
    }

}
