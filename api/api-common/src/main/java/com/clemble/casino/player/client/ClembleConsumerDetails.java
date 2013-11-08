package com.clemble.casino.player.client;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth.common.signature.RSAKeySecret;
import org.springframework.security.oauth.provider.ConsumerDetails;

import com.clemble.casino.utils.CollectionUtils;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class ClembleConsumerDetails implements ConsumerDetails, ConsumerAware {

    /**
     * Generated 06/11/13
     */
    private static final long serialVersionUID = -4209539204058580660L;

    final private String consumerKey;
    final private String consumerName;
    final private RSAKeySecret signatureSecret;
    final private List<GrantedAuthority> authorities;
    final private ClientDetails clientDetail;

    @JsonCreator
    public ClembleConsumerDetails(@JsonProperty("consumerKey") String consumerKey,
            @JsonProperty("consumerName") String consumerName,
            @JsonSerialize(using = RSAKeySecretFormat.RSAKeySecretSerializer.class)
            @JsonDeserialize(using = RSAKeySecretFormat.RSAKeySecretDeserializer.class)
            @JsonProperty("signatureSecret") RSAKeySecret signatureSecret,
            @JsonProperty("authorities") List<GrantedAuthority> authorities,
            @JsonProperty("clientDetail") ClientDetails clientDetails) {
        this.consumerKey = consumerKey;
        this.consumerName = consumerName;
        this.signatureSecret = signatureSecret;
        this.authorities = CollectionUtils.immutableList(authorities);
        this.clientDetail = clientDetails;
    }

    @Override
    public String getConsumerKey() {
        return consumerKey;
    }

    @Override
    public String getConsumerName() {
        return consumerName;
    }

    @Override
    public RSAKeySecret getSignatureSecret() {
        return signatureSecret;
    }

    @Override
    public List<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public ClientDetails getClientDetail() {
        return clientDetail;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((authorities == null) ? 0 : authorities.hashCode());
        result = prime * result + ((clientDetail == null) ? 0 : clientDetail.hashCode());
        result = prime * result + ((consumerKey == null) ? 0 : consumerKey.hashCode());
        result = prime * result + ((consumerName == null) ? 0 : consumerName.hashCode());
        result = prime * result + ((signatureSecret == null) ? 0 : signatureSecret.hashCode());
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
        ClembleConsumerDetails other = (ClembleConsumerDetails) obj;
        if (authorities == null) {
            if (other.authorities != null)
                return false;
        } else if (!authorities.equals(other.authorities))
            return false;
        if (clientDetail == null) {
            if (other.clientDetail != null)
                return false;
        } else if (!clientDetail.equals(other.clientDetail))
            return false;
        if (consumerKey == null) {
            if (other.consumerKey != null)
                return false;
        } else if (!consumerKey.equals(other.consumerKey))
            return false;
        if (consumerName == null) {
            if (other.consumerName != null)
                return false;
        } else if (!consumerName.equals(other.consumerName))
            return false;
        if (signatureSecret == null) {
            if (other.signatureSecret != null)
                return false;
        } else {
            if(signatureSecret.getPrivateKey() == null) { 
                if (other.signatureSecret.getPrivateKey() != null)
                    return false;
            } else if(!signatureSecret.getPrivateKey().equals(other.signatureSecret.getPrivateKey()))
                return false;
            if(signatureSecret.getPublicKey() == null) { 
                if (other.signatureSecret.getPublicKey() != null)
                    return false;
            } else if(!signatureSecret.getPublicKey().equals(other.signatureSecret.getPublicKey()))
                return false;
        }
        return true;
    }

}
