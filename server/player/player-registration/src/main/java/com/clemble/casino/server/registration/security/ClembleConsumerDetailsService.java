package com.clemble.casino.server.registration.security;

import com.clemble.casino.player.client.ClembleConsumerDetails;
import org.springframework.security.oauth.common.OAuthException;
import org.springframework.security.oauth.provider.ConsumerDetailsService;

/**
 * Created by mavarazy on 6/14/14.
 */
public interface ClembleConsumerDetailsService extends ConsumerDetailsService {

    public ClembleConsumerDetails loadConsumerByConsumerKey(String consumerKey);

    public void save(ClembleConsumerDetails clembleConsumerDetails);

}
