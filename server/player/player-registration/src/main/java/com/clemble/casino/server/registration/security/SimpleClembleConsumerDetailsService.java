package com.clemble.casino.server.registration.security;

import com.clemble.casino.security.ClembleConsumerDetails;

import java.util.HashMap;

/**
 * Created by mavarazy on 6/14/14.
 */
public class SimpleClembleConsumerDetailsService implements ClembleConsumerDetailsService {

    final private HashMap<String, ClembleConsumerDetails> consumerKeyToDetails = new HashMap<>();

    @Override
    public ClembleConsumerDetails loadConsumerByConsumerKey(String consumerKey) {
        return consumerKeyToDetails.get(consumerKey);
    }

    @Override
    public void save(ClembleConsumerDetails clembleConsumerDetails) {
        consumerKeyToDetails.put(clembleConsumerDetails.getConsumerKey(), clembleConsumerDetails);
    }

}
